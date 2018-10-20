package com.global.webapp.models;

import java.util.Set;

public class Role {

    public Long id;
    public String name;
    public Set<Long> permissionsIds;
    public String description;
    public Set<String> operationIds;

    public Role() {
    }

    public Role(String name, Set<Long> permissionsIds, String description, Set<String> operationIds) {
        this.name = name;
        this.permissionsIds = permissionsIds;
        this.description = description;
        this.operationIds = operationIds;
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

    public Set<Long> getPermissionsIds() {
        return permissionsIds;
    }

    public void setPermissionsIds(Set<Long> permissionsIds) {
        this.permissionsIds = permissionsIds;
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
