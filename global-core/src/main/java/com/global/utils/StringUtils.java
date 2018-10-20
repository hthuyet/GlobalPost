package com.global.utils;

import java.util.Map;

import static java.lang.Math.max;

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
}
