/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.global.servicebase.user.model;

import java.util.Set;

/**
 *
 * Created by ThuyetLV
 */
public class UserResponse {

    public String userName;
    public String fullName;
    public String email;
    public String phoneNumber;
    public Set<Role> roles;
    public String password;
    public String description;
    public Set<String> operationIds;
    public Long userId;
    public Long branchId;
    public String branchName;
}
