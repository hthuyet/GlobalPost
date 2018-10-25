package com.global.servicebase.globalexception;

/**
 * Created by ThuyetLV
 */
public class ForgotPasswordTokenExpiredException extends RuntimeException {

    public ForgotPasswordTokenExpiredException(String message) {
        super(message);
    }
}
