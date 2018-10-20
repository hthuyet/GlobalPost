package vn.vnpt.ssdc.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import vn.vnpt.ssdc.utils.GenericSerializer;
import vn.vnpt.ssdc.utils.ObjectUtils;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * Created by vietnq on 12/15/16.
 */
public class RedisCache implements ObjectCache {
    private static final Logger logger = LoggerFactory.getLogger(RedisCache.class);
    private RedisTemplate redisTemplate;
    private GenericSerializer serializer;
    private ValueOperations<String,String> valueOperations;

    public RedisCache(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.serializer = new GenericSerializer();
        this.valueOperations = redisTemplate.opsForValue();
    }


    @Override
    public Object get(String key, Class<?> type) {
        String value = valueOperations.get(cacheKey(type,key));
        if(!ObjectUtils.empty(value)) {
            try {
                return serializer.deSerialize(type,value);
            } catch (IOException e) {
                logger.error("Cannot deserialize object of type {}",type.getSimpleName(),e);
            }
        }
        return null;
    }

    @Override
    public Collection<Object> getAll(Set<String> keySet, Class<?> type) {
        return null;
    }

    @Override
    public void put(String key, Object item, Class<?> type) {
        try {
            String value = serializer.serialize(item);
            valueOperations.set(cacheKey(type,key),value);
        } catch (Exception e) {
            logger.error("Cannot serialize object of type {}",type.getSimpleName(),e);
        }
    }

    @Override
    public void put(String key, Object item, int ttl, Class<?> type) {

    }

    @Override
    public void putAll(Map<String, Object> m, Class<?> type) {

    }

    @Override
    public void remove(String key, Class<?> type) {
        redisTemplate.delete(cacheKey(type,key));
    }

    private String cacheKey(Class<?> type, String key) {
        return String.format("%s:%s",type.getSimpleName(),key);
    }
}
