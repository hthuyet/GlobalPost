package com.global.servicecommon.jdbc;

import java.io.Serializable;

/**
 * Created by ThuyetLV
 */
public class SsdcEntity<ID extends Serializable> implements Serializable {
    public ID id;
    public Long created;
    public Long updated;
}
