package com.global.servicebase.globalexception;

public class ObjNotFoundException extends RuntimeException {

    public static final String MESSAGE = "Entity not found";

    public ObjNotFoundException(String message) {
        super(message);
    }
}
