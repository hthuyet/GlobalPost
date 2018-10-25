package com.global.servicebase.globalexception;

/**
 * Created by ThuyetLV
 */
public class InvalidForgotPasswordTokenException extends RuntimeException {

    public InvalidForgotPasswordTokenException(String message) {
        super(message);
    }
}
