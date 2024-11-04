package com.daviozolin.clevertap;

import android.Manifest;
import android.content.pm.PackageManager;
import android.content.Context;
import android.location.Location;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.util.Log;
import com.clevertap.android.geofence.CTGeofenceAPI;
import com.clevertap.android.geofence.CTGeofenceSettings;
import com.clevertap.android.geofence.Logger;
import com.clevertap.android.geofence.interfaces.CTGeofenceEventsListener;
import com.clevertap.android.geofence.interfaces.CTLocationUpdatesListener;
import com.clevertap.android.sdk.CleverTapAPI;
import com.getcapacitor.JSArray;
import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;
import com.getcapacitor.annotation.Permission;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

@CapacitorPlugin(
    name = "CleverTapAnalytics",
    permissions = {
        @Permission(strings = { Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION }, alias = "location"),
        @Permission(strings = { Manifest.permission.ACCESS_BACKGROUND_LOCATION }, alias = "backgroundUpdate")
    }
)
public class CleverTapAnalyticsPlugin extends Plugin {

    CleverTapAPI clevertap;
    CTGeofenceAPI geofence;

    @Override
    public void load() {
        clevertap = CleverTapAPI.getDefaultInstance(getContext().getApplicationContext());
        super.load();
    }

    @PluginMethod
    public void setLogLevel(PluginCall call) {
        JSObject ret = new JSObject();
        Integer level = call.getInt("level");

        ret.put("status", "Log level set to " + level);
        call.resolve(ret);
    }

    @PluginMethod
    public void profileGetID(PluginCall call) {
        JSObject ret = new JSObject();
        String id = clevertap.getCleverTapID();
        ret.put("id", id);
        call.resolve(ret);
    }

    @PluginMethod
    public void recordEvent(PluginCall call) throws JSONException {
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
        String key = call.getString("key");
        if (key == null) {
            call.reject("Key missing or malformatted");
            return;
        }

        Float value = call.getFloat("value");
        if (value == null) {
            call.reject("value details missing or malformatted");
            return;
        }

        clevertap.incrementValue(key, value);

        call.resolve();
    }

    @PluginMethod
    public void triggerLocation(PluginCall call) {
        call.setKeepAlive(true);
        try {
            CTGeofenceAPI.getInstance(getContext().getApplicationContext()).triggerLocation();
        } catch (IllegalStateException e) {
            Log.d("CTGeofence", "exception " + e.getMessage());
        }
        JSObject ret = new JSObject();
        ret.put("value", "New value from Android");
        call.resolve(ret);
    }

    @PluginMethod
    public void profilePush(PluginCall call) throws JSONException {
        JSObject properties = call.getObject("profileProperties");
        HashMap<String, Object> profile = jsObjectToHashMap(properties);

        clevertap.pushProfile(profile);

        call.resolve();
    }

    @PluginMethod
    public void onUserLogin(PluginCall call) throws JSONException {
        JSObject properties = call.getObject("profileProperties");
        HashMap<String, Object> profile = jsObjectToHashMap(properties);

        clevertap.onUserLogin(profile);

        call.resolve();
    }

    @PluginMethod
    public void setLocation(PluginCall call) {
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
        call.resolve();
    }

    @PluginMethod
    public void initGeofence(PluginCall call) {
        try {
            call.setKeepAlive(true);
            Log.d("CTGeofence", "Initializing Clevertap Geofence Plugin");
            Log.d("CTGeofence", "Clevertap instance initiated with " + clevertap.toString());

            CTGeofenceSettings ctGeofenceSettings = new CTGeofenceSettings.Builder()
                .enableBackgroundLocationUpdates(true)
                .setLogLevel(Logger.VERBOSE)
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

            geofence.init(ctGeofenceSettings, clevertap);

            geofence.setOnGeofenceApiInitializedListener(
                new CTGeofenceAPI.OnGeofenceApiInitializedListener() {
                    @Override
                    public void OnGeofenceApiInitialized() {
                        Log.d("CTGeofence", "initialized fence");
                        JSObject ret = new JSObject();
                        ret.put("status", "INITIALIZED");
                        notifyListeners("geofenceInitializedListener", ret);
                    }
                }
            );

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
                }
            );

            geofence.setCtLocationUpdatesListener(
                new CTLocationUpdatesListener() {
                    @Override
                    public void onLocationUpdates(Location location) {
                        JSObject ret = new JSObject();
                        ret.put("lat", location.getLatitude());
                        ret.put("lng", location.getLongitude());
                        notifyListeners("locationUpdateListener", ret);
                    }
                }
            );

            geofence.setCtLocationUpdatesListener(
                new CTLocationUpdatesListener() {
                    @Override
                    public void onLocationUpdates(Location location) {
                        JSObject ret = new JSObject();
                        ret.put("lat", location.getLatitude());
                        ret.put("lng", location.getLongitude());
                        notifyListeners("locationUpdateListener", ret);
                        Log.d("CTGeofence", "new location Lat: " + location.getLatitude() + " and Long: " + location.getLongitude());
                    }
                }
            );

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
}
