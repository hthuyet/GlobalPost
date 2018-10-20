package vn.vnpt.ssdc.utils;

import com.google.gson.Gson;

import java.io.IOException;

/**
 * Created by vietnq on 10/25/16.
 */
public class GenericSerializer implements Serializer<Object> {

    private Gson mapper;

    public GenericSerializer() {
        mapper = new Gson();
    }

    @Override
    public Object deSerialize(Class type, String value) throws IOException {
        return mapper.fromJson(value, type);
    }

    @Override
    public String serialize(Object value) throws Exception {
        return mapper.toJson(value);
    }

    @Override
    public Object deSerializeItem(String value) throws IOException {
        return mapper.fromJson(value, String.class);
    }

    @Override
    public String serializeItem(Object value) throws Exception {
        return mapper.toJson(value);
    }
}
