package com.global.servicecommon.umpexception;

/**
 * Created by ThuyetLV
 */
public class InvalidRequestException extends RuntimeException {

    public InvalidRequestException(String message) {
        super(message);
    }
}
