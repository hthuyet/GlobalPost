package com.global.servicecommon.umpexception;

/**
 * Created by ThuyetLV
 */
public class InvalidForgotPasswordTokenException extends RuntimeException {

    public InvalidForgotPasswordTokenException(String message) {
        super(message);
    }
}
