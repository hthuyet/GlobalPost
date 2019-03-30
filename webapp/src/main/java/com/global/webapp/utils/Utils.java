package com.global.webapp.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Map;

public class Utils {

  private static Logger logger = LoggerFactory.getLogger(Utils.class);

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

  public static String getAsString(Map<String, String> maps, String key, String defaultValue) {
    String rtn = maps.get(key);
    if (rtn == null) {
      return defaultValue;
    }
    return rtn;
  }

  public static Integer getAsInt(Map<String, String> maps, String key, Integer defaultValue) {
    String rtn = maps.get(key);
    try {
      if (rtn == null) {
        return defaultValue;
      }
      return Integer.parseInt(rtn);
    } catch (Exception ex) {
      return defaultValue;
    }
  }

  public static Long getAsLong(Map<String, String> maps, String key, Long defaultValue) {
    String rtn = maps.get(key);
    try {
      if (rtn == null) {
        return defaultValue;
      }
      return Long.parseLong(rtn);
    } catch (Exception ex) {
      return defaultValue;
    }
  }

  public static Boolean getAsBoolean(Map<String, String> maps, String key, Boolean defaultValue) {
    String rtn = maps.get(key);
    if (rtn == null || StringUtils.isBlank(rtn)) {
      return defaultValue;
    }
    return Boolean.parseBoolean(rtn);
  }

  public static void copy(FileInputStream in, FileOutputStream out) {
    try {
      int c;

      while ((c = in.read()) != -1) {
        out.write(c);
      }
    } catch (Exception ex) {
      logger.error("ERROR copy: ", ex);
    }
  }
}
