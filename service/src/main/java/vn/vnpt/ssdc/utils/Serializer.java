package vn.vnpt.ssdc.utils;


import java.io.IOException;

/**
 * Created by vietnq on 10/25/16.
 */
public interface Serializer<T> {
    
    T deSerialize(Class type, String value) throws IOException;
    String serialize(T value) throws Exception;
    
    // Used if the field is a collection
    T deSerializeItem(String value) throws IOException;
    String serializeItem(T value) throws Exception;
}
