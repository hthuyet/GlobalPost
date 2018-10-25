package com.global.servicebase.globalexception;

/**
 * Created by ThuyetLV
 */
public class InvalidRequestException extends RuntimeException {

    public InvalidRequestException(String message) {
        super(message);
    }
}
