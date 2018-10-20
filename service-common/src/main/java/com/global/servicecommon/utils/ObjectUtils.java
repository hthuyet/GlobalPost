package com.global.servicecommon.utils;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import static org.springframework.util.ObjectUtils.toObjectArray;

/**
 * Created by ThuyetLV
 */
public class ObjectUtils {

    public static boolean equals(Set<?> set1, Set<?> set2) {

        if (set1 == null || set2 == null) {
            return false;
        }

        if (set1.size() != set2.size()) {
            return false;
        }

        return set1.containsAll(set2);

    }

    public static Object[] wrapToArray(Object obj) {
        if (obj == null) {
            return new Object[0];
        }
        if (obj instanceof Object[]) {
            return (Object[]) obj;
        }
        if (obj.getClass().isArray()) {
            return toObjectArray(obj);
        }
        return new Object[]{obj};
    }

    public static boolean empty(Object object) {
        if (object == null) {
            return true;
        }
        if (object instanceof String) {
            return "".equals(((String) object).trim());
        } else if (object instanceof Collection) {
            return ((Collection) object).isEmpty();
        } else if (object instanceof Map) {
            return ((Map) object).isEmpty();
        } else if (object instanceof StringBuilder) {
            return ((StringBuilder) object).length() == 0;
        } else if (object instanceof StringBuffer) {
            return ((StringBuffer) object).length() == 0;
        }
        return false;
    }
}
