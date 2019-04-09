package com.global.webapp.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.HashMap;
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

  public static InputStream inputStream(String fileName) throws IOException {
    ClassLoader classLoader = Utils.class.getClassLoader();
    InputStream stream = classLoader.getResourceAsStream(fileName);
    if (stream == null) {
      logger.warn(String.format("File: %s not exits!", fileName));
      stream = Utils.class.getResourceAsStream(fileName);
    }
    if (stream == null) {
      logger.error(String.format("File: %s not exits!", fileName));
      return null;
    }
    return stream;
  }

  public static HashMap<String, String> hm_tien = new HashMap<String, String>() {
    {
      put("0", "không");
      put("1", "một");
      put("2", "hai");
      put("3", "ba");
      put("4", "bốn");
      put("5", "năm");
      put("6", "sáu");
      put("7", "bảy");
      put("8", "tám");
      put("9", "chín");
    }
  };
  public static HashMap<String, String> hm_hanh = new HashMap<String, String>() {
    {
      put("1", "đồng");
      put("2", "mươi");
      put("3", "trăm");
      put("4", "nghìn");
      put("5", "mươi");
      put("6", "trăm");
      put("7", "triệu");
      put("8", "mươi");
      put("9", "trăm");
      put("10", "tỷ");
      put("11", "mươi");
      put("12", "trăm");
      put("13", "nghìn");
      put("14", "mươi");
      put("15", "trăm");

    }
  };

  public static void main(String[] args) throws ParseException {
    String tien = "411304";
    String kq = ChuyenSangChu(tien);
    System.out.println(currencyFormat(tien));
    System.out.println(kq);

    tien = "120000";
    kq = ChuyenSangChu(tien);
    System.out.println(currencyFormat(tien));
    System.out.println(kq);


    tien = "1522456000";
    kq = ChuyenSangChu(tien);
    System.out.println(currencyFormat(tien));
    System.out.println(kq);
  }

  public static String ChuyenSangChu(String x) {
    String kq = "";
    x = x.replace(".", "");
    String arr_temp[] = x.split(",");
    if (!NumberUtils.isNumber(arr_temp[0])) {
      return "";
    }
    String m = arr_temp[0];
    int dem = m.length();
    String dau = "";
    int flag10 = 1;
    while (!m.equals("")) {
      if (m.length() <= 3 && m.length() > 1 && Long.parseLong(m) == 0) {

      } else {
        dau = m.substring(0, 1);
        if (dem % 3 == 1 && m.startsWith("1") && flag10 == 0) {
          kq += "mốt ";
          flag10 = 0;
        } else if (dem % 3 == 2 && m.startsWith("1")) {
          kq += "mười ";
          flag10 = 1;
        } else if (dem % 3 == 2 && m.startsWith("0") && m.length() >= 2 && !m.substring(1, 2).equals("0")) {
          //System.out.println("a  "+m.substring(1, 2));
          kq += "lẻ ";
          flag10 = 1;
        } else {
          if (!m.startsWith("0")) {
            kq += hm_tien.get(dau) + " ";
            flag10 = 0;
          }
        }
        if (dem % 3 != 1 && m.startsWith("0") && m.length() > 1) {
        } else {
          if (dem % 3 == 2 && (m.startsWith("1") || m.startsWith("0"))) {//mười
          } else {
            kq += hm_hanh.get(dem + "") + " ";
          }
        }
      }
      m = m.substring(1);
      dem = m.length();
    }
    kq = kq.substring(0, kq.length() - 1);
    return kq;
  }

  public static String currencyFormat(String curr) {
    try {
      double vaelue = Double.parseDouble(curr);
      String pattern = "###,###";
      DecimalFormat myFormatter = new DecimalFormat(pattern);
      String output = myFormatter.format(vaelue);
      return output;
    } catch (NumberFormatException e) {
      e.printStackTrace();
    }
    return "";
  }
}
