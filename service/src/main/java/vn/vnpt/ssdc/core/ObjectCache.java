package vn.vnpt.ssdc.core;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * Created by vietnq on 10/28/16.
 */
public interface ObjectCache {
    Object get(final String key, Class<?> type);

    Collection<Object> getAll(final Set<String> keySet, Class<?> type);

    void put(final String key, final Object item, Class<?> type);

    void put(final String key, final Object item, final int ttl, Class<?> type);

    void putAll(Map<String, Object> m, Class<?> type);

    void remove(final String key, Class<?> type);

}
