package com.global.webapp.models.searchForm;

import java.util.Set;

public class UserSearchForm {

    public Integer limit;
    public Integer page;
    public String userName;
    public String fullName;
    public String email;
    public String description;
    public String phoneNumber;
    public Set<Integer> deviceGroupIds;
    public Set<Integer> roles;
    public Long userId;
    public String currentPassword;
    public String newPassword;
    public String token;
    public String currentUserName;
    public String redirectUrl;
}
