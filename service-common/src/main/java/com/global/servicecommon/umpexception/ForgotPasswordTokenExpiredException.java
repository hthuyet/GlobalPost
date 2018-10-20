package com.global.servicecommon.umpexception;

/**
 * Created by ThuyetLV
 */
public class ForgotPasswordTokenExpiredException extends RuntimeException {
    public ForgotPasswordTokenExpiredException(String message) {
        super(message);
    }
}
