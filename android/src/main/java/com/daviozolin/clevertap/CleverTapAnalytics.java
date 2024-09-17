package com.daviozolin.clevertap;

import android.util.Log;
import com.clevertap.android.sdk.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import android.location.Location;
import com.getcapacitor.Plugin;


public class CleverTapAnalytics extends Plugin {
    CleverTapAPI clevertap = CleverTapAPI.getDefaultInstance(getContext());
    public String profileGetID() {
        String id = clevertap.getCleverTapID();
        return id != null ? id : "ERROR";
    }

    public void recordEvent(String event, Map<String, Object> properties) {
        clevertap.pushEvent(event, properties);
    }

    public void recordChargedEvent(HashMap<String, Object> details, ArrayList<HashMap<String, Object>> items) {
        clevertap.pushChargedEvent(details, items);
    }

    public void profileIncrementValue(String key, Number value) {
        clevertap.incrementValue(key, value);
    }

    public void profilePush(Map<String, Object> properties) {
        clevertap.pushProfile(properties);
    }

    public void onUserLogin(Map<String, Object> properties) {
        clevertap.onUserLogin(properties);
    }

    public void setLocation(double latitude, double longitude) {
        Location location = new Location("provider");
        location.setLatitude(latitude);
        location.setLongitude(longitude);
        clevertap.setLocation(location);
    }
}
