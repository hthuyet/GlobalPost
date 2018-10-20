package vn.vnpt.ssdc.jdbc;

import java.io.Serializable;

/**
 * Created by vietnq on 10/25/16.
 */
public class SsdcEntity<ID extends Serializable> implements Serializable {
    public ID id;
    public Long created;
    public Long updated;
}
