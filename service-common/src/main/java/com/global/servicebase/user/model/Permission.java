package com.global.servicebase.user.model;

import com.global.jdbc.SsdcEntity;

import java.util.Set;

/**
 * Created by ThuyetLV
 */
public class Permission extends SsdcEntity<Long> {

    public String name;
    public String groupName;
    public String description;
    public Set<String> operationIds;

}
