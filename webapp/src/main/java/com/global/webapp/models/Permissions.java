package com.global.webapp.models;

import java.util.Set;

/**
 * Created by ThuyetLV
 */
public class Permissions {

    private Long id;
    private String name;
    private String groupName;
    private String description;
    private Set<String> operationIds;

    public Permissions() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<String> getOperationIds() {
        return operationIds;
    }

    public void setOperationIds(Set<String> operationIds) {
        this.operationIds = operationIds;
    }
}
