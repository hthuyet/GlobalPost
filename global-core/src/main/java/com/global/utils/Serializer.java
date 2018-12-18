package com.global.utils;

import java.io.IOException;

/**
 * Created by ThuyetLV
 */
public interface Serializer<T> {

    T deSerialize(Class type, String value) throws IOException;

    String serialize(T value) throws Exception;

    // Used if the field is a collection
    T deSerializeItem(String value) throws IOException;

    String serializeItem(T value) throws Exception;
}
