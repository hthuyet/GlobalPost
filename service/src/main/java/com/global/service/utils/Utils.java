package com.global.service.utils;

import com.global.service.model.BillResponse;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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


  public static HashMap<String, String> createListCode(String input) {
    HashMap<String, String> listCode = new HashMap<String, String>();
    if (input.contains(";") || input.contains("-")) {
      String[] tmp = input.split(";");
      String[] tmp2;
      long i = 0L;
      long j = 0L;
      String start = "";
      String end = "";
      for (String item : tmp) {
        if (item.contains("-")) {
          tmp2 = item.split("-");
          start = Utils.getLastDigits(tmp2[0]);
          end = Utils.getLastDigits(tmp2[1]);

          if (start.trim().length() > 0 && end.trim().length() > 0) {
            i = Long.valueOf(start);
            j = Long.valueOf(end);

            start = tmp2[0].substring(0, tmp2[0].length() - start.length());
            for (i = i; i <= j; i++) {
              listCode.put(start + String.valueOf(i), start + String.valueOf(i));
            }
          }else{
            listCode.put(tmp2[0], tmp2[0]);
            listCode.put(tmp2[1], tmp2[1]);
          }

        } else {
          listCode.put(item, item);
        }
      }
    } else {
      listCode.put(input, input);
    }
    return listCode;
  }

  public static String getLastDigits(String str) {
    Pattern p = Pattern.compile("[0-9]+$");
    Matcher m = p.matcher(str);
    if (m.find()) {
      return m.group();
    }
    return "";
  }

  public static void main(String[] args) {
    String s = "sdnfasdfjksa13579";
    System.out.println(String.format("Test {%s}: {%s}", s, Utils.getLastDigits(s)));


    System.out.println("-----" + s.substring(0, s.length() - Utils.getLastDigits(s).length()));
    s = " abc88";
    System.out.println(String.format("Test {%s}: {%s}", s, Utils.getLastDigits(s)));

    System.out.println("-----" + s.substring(0, s.length() - Utils.getLastDigits(s).length()));

    s = "abc9999";
    System.out.println(String.format("Test {%s}: {%s}", s, Utils.getLastDigits(s)));

    s = "abc9999bc";
    System.out.println(String.format("Test {%s}: {%s}", s, Utils.getLastDigits(s)));


    s = "a;b;c;abc13578-abc13582";

    HashMap<String, String> test = Utils.createListCode(s);
    for(Map.Entry<String, String> entry : test.entrySet()) {
      String key = entry.getKey();
      System.out.println("---" + key);
    }
  }

}
