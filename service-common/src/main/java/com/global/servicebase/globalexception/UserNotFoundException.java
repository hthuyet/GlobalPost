package com.global.servicebase.globalexception;

/**
 * Created by ThuyetLV
 */
public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(String message) {
        super(message);
    }
}
