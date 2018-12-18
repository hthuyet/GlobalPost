package com.global.jdbc;

import java.io.Serializable;

/**
 * Created by ThuyetLV
 */
public class SsdcEntity<ID extends Serializable> implements Serializable {

    public ID id;
    public Long created;
    public Long updated;

    public ID getId() {
        return id;
    }

    public void setId(ID id) {
        this.id = id;
    }

    public Long getCreated() {
        return created;
    }

    public void setCreated(Long created) {
        this.created = created;
    }

    public Long getUpdated() {
        return updated;
    }

    public void setUpdated(Long updated) {
        this.updated = updated;
    }
}
