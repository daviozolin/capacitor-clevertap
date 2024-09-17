package com.daviozolin.clevertap;

import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;

import java.util.Map;
import java.util.List;

@CapacitorPlugin(name = "CleverTapAnalytics")
public class CleverTapAnalyticsPlugin extends Plugin {

    CleverTapAnalytics implementation = new CleverTapAnalytics();

    @PluginMethod
    public void profileGetID(PluginCall call) {
        JSObject ret = new JSObject();
        ret.put("id", implementation.profileGetID());
        call.resolve(ret);
    }

    public void recordEvent(String event, Map<String, Object> properties) {
        
    }

    public void recordChargedEvent(Map<String, Object> details, List<Map<String, Object>> items) {
        
    }

    public void profileIncrementValue(String key, Number value) {
        
    }

    public void profilePush(Map<String, Object> properties) {
        
    }

    public void onUserLogin(Map<String, Object> properties) {
       
    }

    public void setLocation(double latitude, double longitude) {
        
    }

    public void setPushTokenAs(String token) {
        
    }
}
