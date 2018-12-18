package com.global.exception;

public class ObjExitsException extends RuntimeException {

    public static final String MESSAGE = "Entity exits!";

    public ObjExitsException(String message) {
        super(message);
    }
}
