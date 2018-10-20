package com.global.servicecommon.user.model;

import org.mindrot.jbcrypt.BCrypt;
import com.global.servicecommon.jdbc.SsdcEntity;

import java.util.LinkedHashSet;
import java.util.Set;


public class User extends SsdcEntity<Long> {

    public String userName;
    public String fullName;
    public String email;
    public String password;
    public Set<String> roleIds;
    public Set<String> roleNames;
    public Set<String> operationIds;
    public String avatarUrl;
    public String phone;
    public String description;
    public String forgotPwdToken;
    public Long forgotPwdTokenRequested;

    public User() {
        this.roleIds = new LinkedHashSet<String>();
        this.roleNames = new LinkedHashSet<String>();
        this.operationIds = new LinkedHashSet<String>();
    }

    public void setEncryptedPassword(String password) {
        this.password = encryptedPassword(password);
    }

    private String encryptedPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }
}