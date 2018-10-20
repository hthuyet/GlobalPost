package com.global.servicecommon.user.model;

import com.global.servicecommon.jdbc.SsdcEntity;

import java.util.Set;

/**
 * Created by ThuyetLV
 */
public class Role extends SsdcEntity<Long> {

    public String name;
    public Set<Long> permissionsIds;
    public String description;
    public Set<String> operationIds;
}
