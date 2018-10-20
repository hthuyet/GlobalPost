package com.global.servicecommon.umpexception;

/**
 * Created by ThuyetLV
 */
public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(String message) {
        super(message);
    }
}
