package com.daviozolin.clevertap;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.Context;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.app.NotificationManager;
import android.util.Log;
import com.clevertap.android.geofence.CTGeofenceAPI;
import com.clevertap.android.geofence.CTGeofenceSettings;
import com.clevertap.android.geofence.interfaces.CTGeofenceEventsListener;
import com.clevertap.android.geofence.interfaces.CTLocationUpdatesListener;
import com.clevertap.android.sdk.pushnotification.CTPushNotificationListener;
import com.clevertap.android.sdk.CleverTapAPI;
import com.clevertap.android.sdk.Utils;
import com.getcapacitor.JSArray;
import com.getcapacitor.JSObject;
import com.getcapacitor.PermissionState;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;
import com.getcapacitor.annotation.Permission;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

@CapacitorPlugin(name = "CleverTapAnalytics", permissions = {
        @Permission(strings = { Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION }, alias = "location"),
        @Permission(strings = { Manifest.permission.ACCESS_BACKGROUND_LOCATION }, alias = "backgroundUpdate")
})
public class CleverTapAnalyticsPlugin extends Plugin implements CTPushNotificationListener {

    // Present on every CleverTap push (Constants.NOTIFICATION_TAG / NOTIFICATION_ID_TAG in the SDK).
    private static final String CLEVERTAP_PUSH_KEY = "wzrk_pn";
    private static final String CLEVERTAP_PUSH_ID_KEY = "wzrk_id";
    private static final long PUSH_CLICK_DEDUPE_WINDOW_MS = 5000;

    // Static so they survive the Bridge (and this plugin) being recreated with the activity.
    private static WeakReference<Intent> lastPushIntent = new WeakReference<>(null);
    private static Object lastPushClickId;
    private static long lastPushClickTime;

    CleverTapAPI clevertap;
    CTGeofenceAPI geofence;

    @Override
    public void load() {
        super.load();
        clevertap = CleverTapAPI.getDefaultInstance(getContext().getApplicationContext());
        if (clevertap != null) {
            clevertap.setCTPushNotificationListener(this);
        }
    }

    @Override
    protected void handleOnNewIntent(Intent intent) {
        super.handleOnNewIntent(intent);
        Log.d("CleverTapCustomPlugin", "handleOnNewIntent called");
        Bundle extras = intent.getExtras();
        if (clevertap != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            clevertap.pushNotificationClickedEvent(extras);
        }

        // CleverTap's click callback misses taps that launch the app: with ActivityLifecycleCallback
        // registered, CleverTap handles them in Activity.onCreate(), before plugins are loaded. The push
        // extras are on the launch intent, which BridgeActivity passes here once plugins are loaded.
        if (extras == null || !extras.containsKey(CLEVERTAP_PUSH_KEY)) {
            return;
        }
        // Reopening the task from Recents re-delivers the original push intent, and so does
        // BridgeActivity when the activity is recreated; neither is a new tap.
        if ((intent.getFlags() & Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY) != 0 || intent == lastPushIntent.get()) {
            return;
        }
        lastPushIntent = new WeakReference<>(intent);
        emitPushClicked(Utils.convertBundleObjectToHashMap(extras));
    }

    @Override
    public void onNotificationClickedPayloadReceived(HashMap<String, Object> hashMap) {
        emitPushClicked(hashMap);
    }

    private void emitPushClicked(HashMap<String, Object> hashMap) {
        // A tap on a running app is reported by both CleverTap's callback and the activity intent.
        synchronized (CleverTapAnalyticsPlugin.class) {
            Object id = hashMap.get(CLEVERTAP_PUSH_ID_KEY);
            long now = SystemClock.elapsedRealtime();
            if (id != null && id.equals(lastPushClickId) && now - lastPushClickTime < PUSH_CLICK_DEDUPE_WINDOW_MS) {
                return;
            }
            lastPushClickId = id;
            lastPushClickTime = now;
        }

        JSObject data = new JSObject();

        try {
            data = toJSObject(hashMap);
        } catch (Exception e) {
            // fallback to empty object if conversion fails
            data = new JSObject();
        }

        try {
            data.put("image", data.get("wzrk_bpds"));
        } catch (Exception ignored) {
        }

        try {
            data.put("body", data.get("nm"));
        } catch (Exception ignored) {
        }

        try {
            data.put("title", data.get("nt"));
        } catch (Exception ignored) {
        }

        try {
            // Retain until a listener is attached: a tap that cold-starts the app
            // arrives before the web app has had a chance to call addListener().
            notifyListeners("onPushClicked", data, true);
        } catch (Exception ignored) {
        }
    }

    /**
     * Rejects the call when there is no CleverTap instance, which happens when
     * CLEVERTAP_ACCOUNT_ID / CLEVERTAP_TOKEN are missing from AndroidManifest.xml.
     */
    private boolean ensureCleverTap(PluginCall call) {
        if (clevertap == null) {
            call.reject("CleverTap is not initialized. Check CLEVERTAP_ACCOUNT_ID and CLEVERTAP_TOKEN in AndroidManifest.xml");
            return false;
        }
        return true;
    }

    @PluginMethod
    public void setDebugLevel(PluginCall call) {
        Integer level = call.getInt("level");
        if (level == null) {
            call.reject("Debug level missing or malformatted");
            return;
        }

        // DEBUG_LEVEL values (-1, 0, 2, 3) match CleverTapAPI.LogLevel on Android.
        CleverTapAPI.setDebugLevel(level);

        JSObject ret = new JSObject();
        ret.put("status", "Log level set to " + level);
        call.resolve(ret);
    }

    @PluginMethod
    public void profileGetID(PluginCall call) {
        if (!ensureCleverTap(call)) {
            return;
        }

        JSObject ret = new JSObject();
        String id = clevertap.getCleverTapID();
        ret.put("id", id);
        call.resolve(ret);
    }

    @PluginMethod
    public void recordEvent(PluginCall call) throws JSONException {
        if (!ensureCleverTap(call)) {
            return;
        }

        String event = call.getString("event");
        if (event == null) {
            call.reject("Event name missing or malformatted");
            return;
        }

        JSObject props = call.getObject("properties");
        if (props == null) {
            call.reject("Properties missing or malformatted");
            return;
        }
        clevertap.pushEvent(event, jsObjectToMap(props));

        call.resolve();
    }

    @PluginMethod
    public void recordChargedEvent(PluginCall call) {
        if (!ensureCleverTap(call)) {
            return;
        }

        JSObject details = call.getObject("details");
        if (details == null) {
            call.reject("Purchase details missing or malformatted");
            return;
        }

        JSArray itemsArray = call.getArray("items");

        if (itemsArray == null) {
            call.reject("Items missing or malformatted");
            return;
        }

        try {
            HashMap<String, Object> chargeDetails = jsObjectToHashMap(details);
            ArrayList<HashMap<String, Object>> items = convertJSArrayToArrayList(itemsArray);

            clevertap.pushChargedEvent(chargeDetails, items);

            call.resolve();
        } catch (Exception e) {
            call.reject("Error processing items array: " + e.getMessage());
        }
    }

    @PluginMethod
    public void profileIncrementValue(PluginCall call) {
        if (!ensureCleverTap(call)) {
            return;
        }

        String key = call.getString("key");
        if (key == null) {
            call.reject("Key missing or malformatted");
            return;
        }

        // Read the raw Number: getDouble() returns null for integers outside the int range (they arrive as Long).
        Object value = call.getData().opt("value");
        if (!(value instanceof Number)) {
            call.reject("value details missing or malformatted");
            return;
        }

        clevertap.incrementValue(key, (Number) value);

        call.resolve();
    }

    @PluginMethod
    public void triggerLocation(PluginCall call) {
        // The Geofence SDK silently ignores triggerLocation() before init() or without location permission.
        if (geofence == null) {
            call.reject("initGeofence() must be called before triggerLocation()");
            return;
        }
        if (getPermissionState("location") != PermissionState.GRANTED) {
            call.reject("Location permission not granted");
            return;
        }
        try {
            geofence.triggerLocation();
        } catch (IllegalStateException e) {
            // Thrown after stopGeofence() has deactivated the SDK.
            call.reject(e.getMessage());
            return;
        }
        call.resolve();
    }

    @PluginMethod
    public void profilePush(PluginCall call) throws JSONException {
        if (!ensureCleverTap(call)) {
            return;
        }

        JSObject properties = call.getObject("profileProperties");
        if (properties == null) {
            call.reject("Profile properties details missing or malformatted");
            return;
        }
        HashMap<String, Object> profile = jsObjectToHashMap(properties);

        clevertap.pushProfile(profile);

        call.resolve();
    }

    @PluginMethod
    public void onUserLogin(PluginCall call) throws JSONException {
        if (!ensureCleverTap(call)) {
            return;
        }

        JSObject properties = call.getObject("profileProperties");
        if (properties == null) {
            call.reject("Profile properties details missing or malformatted");
            return;
        }
        HashMap<String, Object> profile = jsObjectToHashMap(properties);

        clevertap.onUserLogin(profile);

        call.resolve();
    }

    @PluginMethod
    public void setLocation(PluginCall call) {
        if (!ensureCleverTap(call)) {
            return;
        }

        Double lat = call.getDouble("lat");
        Double lng = call.getDouble("lng");

        if (lat == null) {
            call.reject("Latitude missing or malformatted");
            return;
        }

        if (lng == null) {
            call.reject("Longitude missing or malformatted");
            return;
        }

        Location location = new Location("gps");
        location.setLatitude(lat);
        location.setLongitude(lng);

        clevertap.setLocation(location);

        call.resolve();
    }

    @PluginMethod
    public void setPushTokenAs(PluginCall call) {
        if (!ensureCleverTap(call)) {
            return;
        }

        String token = call.getString("token");
        if (token == null) {
            call.reject("Push token missing or malformatted");
            return;
        }

        clevertap.pushFcmRegistrationId(token, true);

        call.resolve();
    }

    @PluginMethod
    public void initGeofence(PluginCall call) {
        if (!ensureCleverTap(call)) {
            return;
        }

        try {
            Log.d("CTGeofence", "Initializing Clevertap Geofence Plugin");
            Log.d("CTGeofence", "Clevertap instance initiated with " + clevertap.toString());

            CTGeofenceSettings ctGeofenceSettings = new CTGeofenceSettings.Builder()
                    .enableBackgroundLocationUpdates(true)
                    // Geofence Logger levels share CleverTapAPI.LogLevel's values.
                    .setLogLevel(CleverTapAPI.getDebugLevel())
                    .setLocationAccuracy(CTGeofenceSettings.ACCURACY_HIGH)
                    .setLocationFetchMode(CTGeofenceSettings.FETCH_CURRENT_LOCATION_PERIODIC)
                    .setGeofenceMonitoringCount(50)
                    .setInterval(30 * 60 * 1000)
                    .setFastestInterval(30 * 60 * 1000)
                    .setSmallestDisplacement(200)
                    .setGeofenceNotificationResponsiveness(0)
                    .build();

            Context context = getContext().getApplicationContext();
            geofence = CTGeofenceAPI.getInstance(context);

            // Listeners go in before init(): init() starts the async task that
            // fires OnGeofenceApiInitialized.
            geofence.setOnGeofenceApiInitializedListener(
                    new CTGeofenceAPI.OnGeofenceApiInitializedListener() {
                        @Override
                        public void OnGeofenceApiInitialized() {
                            Log.d("CTGeofence", "initialized fence");
                            JSObject ret = new JSObject();
                            ret.put("status", "INITIALIZED");
                            notifyListeners("geofenceInitializedListener", ret);
                        }
                    });

            geofence.setCtGeofenceEventsListener(
                    new CTGeofenceEventsListener() {
                        @Override
                        public void onGeofenceEnteredEvent(JSONObject jsonObject) {
                            Log.d("CTGeofence", "onGeofenceEnteredEvent triggered");
                            notifyListeners("geofenceEnteredListener", JSONObjectToJSObject(jsonObject));
                        }

                        @Override
                        public void onGeofenceExitedEvent(JSONObject jsonObject) {
                            Log.d("CTGeofence", "onGeofenceExitedEvent triggered");
                            notifyListeners("geofenceExitedListener", JSONObjectToJSObject(jsonObject));
                        }
                    });

            geofence.setCtLocationUpdatesListener(
                    new CTLocationUpdatesListener() {
                        @Override
                        public void onLocationUpdates(Location location) {
                            JSObject ret = new JSObject();
                            ret.put("lat", location.getLatitude());
                            ret.put("lng", location.getLongitude());
                            notifyListeners("locationUpdateListener", ret);
                        }
                    });

            geofence.init(ctGeofenceSettings, clevertap);

            call.resolve();
        } catch (IllegalStateException e) {
            call.reject("Initialization failed");
        }
    }

    @PluginMethod
    public void stopGeofence(PluginCall call) {
        try {
            geofence.deactivate();
            call.resolve();
        } catch (Exception e) {
            call.reject("Error in deactivating Geofence");
        }
    }

    public Map<String, Object> jsObjectToMap(JSObject jsObject) throws JSONException {
        Map<String, Object> map = new HashMap<>();
        Iterator<String> keys = jsObject.keys();

        while (keys.hasNext()) {
            String key = keys.next();
            Object value = jsObject.get(key);
            map.put(key, value);
        }

        return map;
    }

    @NonNull
    private HashMap<String, Object> jsObjectToHashMap(@NonNull JSObject jsObject) throws JSONException {
        HashMap<String, Object> hashMap = new HashMap<>();
        Iterator<String> keys = jsObject.keys();

        while (keys.hasNext()) {
            String key = keys.next();
            Object value = jsObject.get(key);
            hashMap.put(key, value);
        }

        return hashMap;
    }

    public JSObject JSONObjectToJSObject(JSONObject jsonObject) {
        JSObject jsObject = new JSObject();
        for (Iterator<String> it = jsonObject.keys(); it.hasNext();) {
            String key = it.next();
            try {
                Object value = jsonObject.get(key);
                jsObject.put(key, value);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return jsObject;
    }

    public ArrayList<HashMap<String, Object>> convertJSArrayToArrayList(JSArray jsArray) throws JSONException {
        ArrayList<HashMap<String, Object>> arrayList = new ArrayList<>();

        for (int i = 0; i < jsArray.length(); i++) {
            JSONObject jsonObject = jsArray.getJSONObject(i);
            HashMap<String, Object> map = new HashMap<>();

            Iterator<String> keys = jsonObject.keys();

            while (keys.hasNext()) {
                String key = keys.next();
                map.put(key, jsonObject.get(key));
            }

            arrayList.add(map);
        }

        return arrayList;
    }

    public static JSObject toJSObject(HashMap<String, Object> map) {
        JSObject jsObject = new JSObject();

        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            if (value instanceof String) {
                jsObject.put(key, (String) value);
            } else if (value instanceof Integer) {
                jsObject.put(key, (Integer) value);
            } else if (value instanceof Double) {
                jsObject.put(key, (Double) value);
            } else if (value instanceof Boolean) {
                jsObject.put(key, (Boolean) value);
            } else if (value instanceof Float) {
                jsObject.put(key, ((Float) value).doubleValue());
            } else if (value instanceof Long) {
                jsObject.put(key, ((Long) value).doubleValue());
            } else {
                jsObject.put(key, value != null ? value.toString() : null);
            }
        }

        return jsObject;
    }
}
