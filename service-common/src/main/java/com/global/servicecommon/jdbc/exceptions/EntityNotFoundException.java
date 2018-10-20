package com.global.servicecommon.jdbc.exceptions;

import java.io.Serializable;

import static java.lang.String.format;

/**
 * Created by ThuyetLV
 */
public class EntityNotFoundException extends RuntimeException {
    public String tableName;
    public Serializable id;
    public EntityNotFoundException(String tableName, Serializable id) {
        super(format("No entity with id #{%s} in table %s",
                id, tableName));
        this.tableName = tableName;
        this.id = id;
    }

    public EntityNotFoundException(String tableName, String msg) {
        super(msg);
        this.tableName = tableName;
        this.id = new Object[0];
    }

    public EntityNotFoundException(String tableName, String msg, Throwable cause) {
        super(msg, cause);
        this.tableName = tableName;
        this.id = new Object[0];
    }
}
