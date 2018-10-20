/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.vnpt.ssdc.user.model;

import io.swagger.annotations.ApiModelProperty;
import java.util.Set;

/**
 *
 * @author Admin
 */
public class UserSearchForm {

    @ApiModelProperty(example = "getAll ~ limit = null & page = null")
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

    @ApiModelProperty(example = "using for checkCurrentPassword API")
    public String currentPassword;
    @ApiModelProperty(example = "using for changePass API")
    public String newPassword;
    @ApiModelProperty(example = "token for change pass")
    public String token;

    public String currentUserName;
    public String redirectUrl;

}
