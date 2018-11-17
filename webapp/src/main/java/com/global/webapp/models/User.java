package com.global.webapp.models;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created by ThuyetLV
 */
public class User {

    public String userName;
    public String fullName;
    public String email;
    public String password;
    public Set<String> roleIds;
    public Set<String> roleNames;
    public Set<String> deviceGroupIds;
    public Set<String> deviceGroupNames;
    public String avatarUrl;
    public String phoneNumber;
    public String description;
    public String forgotPwdToken;
    public String forgotPwdTokenRequested;
    public Set<String> operationIds;
    public Set<Role> roles;
    public Long userId;
    public Long branchId;
    public String branchName;

    public User() {
        this.roleIds = new LinkedHashSet<>();
        this.roleNames = new LinkedHashSet<String>();
        this.deviceGroupIds = new LinkedHashSet<String>();
        this.deviceGroupNames = new LinkedHashSet<String>();
        this.operationIds = new LinkedHashSet<String>();
    }
}
