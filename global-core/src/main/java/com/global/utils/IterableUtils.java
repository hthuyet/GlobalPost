package com.global.utils;

import java.util.ArrayList;
import java.util.List;

/**
* Created by ThuyetLV
 */
public class IterableUtils {

    public static <T> List<T> toList(Iterable<T> iterable) {

        if (iterable instanceof List) {
            return (List<T>) iterable;
        }

        List<T> result = new ArrayList<T>();
        for (T item : iterable) {
            result.add(item);
        }

        return result;
    }
}
