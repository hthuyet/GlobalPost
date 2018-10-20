package com.global.webapp.utils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;

public class StringUtils {

    public static Date convertStringToDate(String dateTime, String fromFormat) {
        Date result = null;
        try {

            // Declare format
            SimpleDateFormat simpleDateFormatFrom = new SimpleDateFormat(fromFormat);

            // Parse string date time
            result = simpleDateFormatFrom.parse(dateTime);

        } catch (Exception e) {
        }

        return result;
    }

    public static Date convertStringToDate(String dateTime, String fromFormat, TimeZone fromTimeZone) {
        Date result = null;
        try {

            // Declare format
            SimpleDateFormat simpleDateFormatFrom = new SimpleDateFormat(fromFormat);

            // Set time zone
            simpleDateFormatFrom.setTimeZone(fromTimeZone);

            // Parse string date time
            result = simpleDateFormatFrom.parse(dateTime);

        } catch (Exception e) {
        }

        return result;
    }

    public static String convertStringToDateFormat(String dateTime, String fromFormat, String toFormat) {
        String result = null;
        try {

            // Declare format
            SimpleDateFormat simpleDateFormatFrom = new SimpleDateFormat(fromFormat);
            SimpleDateFormat simpleDateFormatTo = new SimpleDateFormat(toFormat);

            // Parse string date time
            Date date = simpleDateFormatFrom.parse(dateTime);
            result = simpleDateFormatTo.format(date);

        } catch (Exception e) {
        }

        return result;
    }

    public static String convertStringToDateFormat(String dateTime, String fromFormat, String toFormat, TimeZone fromTimeZone, TimeZone toTimeZone) {
        String result = null;
        try {

            // Declare format
            SimpleDateFormat simpleDateFormatFrom = new SimpleDateFormat(fromFormat);
            SimpleDateFormat simpleDateFormatTo = new SimpleDateFormat(toFormat);

            // Set time zone
            simpleDateFormatFrom.setTimeZone(fromTimeZone);
            simpleDateFormatTo.setTimeZone(toTimeZone);

            // Parse string date time
            Date date = simpleDateFormatFrom.parse(dateTime);
            result = simpleDateFormatTo.format(date);

        } catch (Exception e) {
        }

        return result;
    }

    public static String convertTimestampToDateFormat(long dateTime, String toFormat) {
        String result = null;
        try {
            // Declare format
            SimpleDateFormat simpleDateFormatTo = new SimpleDateFormat(toFormat);

            // Parse long timestamp
            Timestamp timestamp = new Timestamp(dateTime);
            result = simpleDateFormatTo.format(timestamp);

        } catch (Exception e) {
        }

        return result;
    }

    public static String queryStringFromMap(Map<String, String> map) {
        if (map.size() > 0) {
            StringBuilder sb = new StringBuilder("");
            for (String key : map.keySet()) {
                sb.append(String.format("&%s={%s}", key, key));
            }
            //remove first "&" char
            return sb.substring(1);
        }
        return "";
    }

    public static String convertStringToTr069Parameter(String path) {
        return path.replaceAll("\\.(\\d+)\\.", ".{i}.");
    }
}
