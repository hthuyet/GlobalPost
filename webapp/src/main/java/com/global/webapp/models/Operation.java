package com.global.webapp.models;

/**
 * Created by ThuyetLV
 */
public class Operation {

    private String id;
    private String name;
    private String groupName;
    private String description;
    private Long created;
    private Long updated;

    public Operation(String name, String groupName, String description) {
        this.name = name;
        this.groupName = groupName;
        this.description = description;
    }

    public Operation() {
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
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
}
