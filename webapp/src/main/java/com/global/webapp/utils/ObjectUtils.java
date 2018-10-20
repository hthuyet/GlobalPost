package com.global.webapp.utils;

import java.util.Collection;
import java.util.Map;

import static org.springframework.util.ObjectUtils.toObjectArray;

public class ObjectUtils {
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
