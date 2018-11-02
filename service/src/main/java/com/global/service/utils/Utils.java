package com.global.service.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class Utils {

    public static String getAsString(JsonObject obj, String key, String defaultValue) {
        if (obj.get(key) == null || obj.get(key).isJsonNull()) {
            return defaultValue;
        }
        return obj.get(key).getAsString();
    }

    public static Integer getAsInt(JsonObject obj, String key, Integer defaultValue) {
        if (obj.get(key) == null || obj.get(key).isJsonNull()) {
            return defaultValue;
        }
        return obj.get(key).getAsInt();
    }

    public static Long getAsLong(JsonObject obj, String key, Long defaultValue) {
        if (obj.get(key) == null || obj.get(key).isJsonNull()) {
            return defaultValue;
        }
        return obj.get(key).getAsLong();
    }

    public static JsonObject getAsJsonObject(JsonObject obj, String key, JsonObject defaultValue) {
        if (obj.get(key) == null || obj.get(key).isJsonNull()) {
            return defaultValue;
        }
        return obj.get(key).getAsJsonObject();
    }

    public static JsonArray getAsJsonArray(JsonObject obj, String key, JsonArray defaultValue) {
        if (obj.get(key) == null || obj.get(key).isJsonNull()) {
            return defaultValue;
        }
        return obj.get(key).getAsJsonArray();
    }

}
