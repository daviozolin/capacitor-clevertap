package com.daviozolin.clevertap;

import android.location.Location;
import androidx.annotation.NonNull;
import com.clevertap.android.sdk.CleverTapAPI;
import com.getcapacitor.JSArray;
import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

@CapacitorPlugin(name = "CleverTapAnalytics")
public class CleverTapAnalyticsPlugin extends Plugin {

    CleverTapAPI clevertap;

    @Override
    public void load() {
        clevertap = CleverTapAPI.getDefaultInstance(getContext());
        super.load();
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
    private List<Map<String, Object>> jsObjectListToMapList(@NonNull List<JSObject> jsObjectList) throws JSONException {
        List<Map<String, Object>> mapList = new ArrayList<>();
        for (JSObject jsObject : jsObjectList) {
            mapList.add(jsObjectToMap(jsObject));
        }
        return mapList;
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
