package com.global.servicecommon.utils;

import java.util.Map;

import static java.lang.Math.max;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by ThuyetLV
 */
public class StringUtils {

    public static String repeat(String str, String separator, int count) {
        StringBuilder sb = new StringBuilder((str.length() + separator.length()) * max(count, 0));

        for (int n = 0; n < count; n++) {
            if (n > 0) {
                sb.append(separator);
            }
            sb.append(str);
        }
        return sb.toString();
    }

    /**
     * return string in snake case
     *
     * @param input a String, for ex: userName
     * @return string in snake case, for ex: user_name
     */
    public static String toSnakeCase(String input) {
        if (input == null) {
            return "";
        }
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (Character.isUpperCase(c)) {
                if (i == 0) {
                    result.append(Character.toLowerCase(c));
                } else {
                    result.append('_').append(Character.toLowerCase(c));
                }
            } else {
                result.append(c);
            }
        }
        return result.toString();
    }

    /**
     * returns url query with placeholders to use when call RestTemplate
     *
     * @param map a Map, for ex: {"limit":"5","offset":"0"}
     * @return url query, for ex: limit={limit}&offset={offset}
     */
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

    public static String convertDate(String date, String fromFormat, String toFormat) {
        String result = null;
        try {

            // Declare date
            DateFormat dateFormat = new SimpleDateFormat(fromFormat);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(toFormat);

            // Convert date time
            Date dateDf = dateFormat.parse(date);
            result = simpleDateFormat.format(dateDf);

        } catch (ParseException e) {
            result = null;
            e.printStackTrace();
        }

        return result;
    }

    public static String convertDateWithTimeZone(String date, String fromFormat, TimeZone fromTimeZone, String toFormat, TimeZone toTimeZone) {
        String result = null;
        try {

            // Declare date
            DateFormat dateFormat = new SimpleDateFormat(fromFormat);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(toFormat);

            // Set time zone
            dateFormat.setTimeZone(fromTimeZone);
            simpleDateFormat.setTimeZone(toTimeZone);

            // Convert date time
            Date dateDf = dateFormat.parse(date);
            result = simpleDateFormat.format(dateDf);

        } catch (ParseException e) {
            result = null;
            e.printStackTrace();
        }

        return result;
    }

    // Convert from zone +0 to zone default
    public static String convertDateFromElk(String date, String fromFormat, String toFormat) {
        return convertDateWithTimeZone(date, fromFormat, TimeZone.getTimeZone("GMT+0"), toFormat, TimeZone.getDefault());
    }

    // Convert from zone default to zone +0
    public static String convertDateToElk(String date, String fromFormat, String toFormat) {
        return convertDateWithTimeZone(date, fromFormat, TimeZone.getDefault(), toFormat, TimeZone.getTimeZone("GMT+0"));
    }

    public static String convertDateToElk1(String date, String fromFormat, String toFormat) {
        return convertDateWithTimeZone(date, fromFormat, TimeZone.getDefault(), toFormat, TimeZone.getTimeZone("GMT+7"));
    }

    public static Long convertDatetimeToTimestamp(String dateTime, String fromFormat) {
        Long result = null;
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(fromFormat);
            Date date = dateFormat.parse(dateTime);
            Timestamp timestamp = new Timestamp(date.getTime());
            result = timestamp.getTime();
        } catch (ParseException e) {
            result = null;
            e.printStackTrace();
        }
        return result;
    }

    public static String convertDateToString(int interval) {
        Calendar cal = Calendar.getInstance();
        if (interval > 0) {
            cal.add(Calendar.MINUTE, -interval);
        }
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(cal.getTime());
    }

    public static long convertDateToLong(String date, String format) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        // Convert date time
        Date dateDf = simpleDateFormat.parse(date);
        return dateDf.getTime();
    }

    public static String convertToUtf8(String str) {
        byte[] byteText = str.getBytes(Charset.forName("UTF-8"));
        String originalString = "";
        try {
            originalString = new String(byteText, "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
        }
        return originalString;
    }

    public static String toZoneDateTime(String value) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        LocalDateTime dateTime = LocalDateTime.parse(value, formatter);

        // Add zone time
        ZonedDateTime zonedDateTime = ZonedDateTime.of(dateTime.toLocalDate(), dateTime.toLocalTime(), ZoneId.systemDefault());
        return zonedDateTime.toOffsetDateTime().toString();
    }
}
