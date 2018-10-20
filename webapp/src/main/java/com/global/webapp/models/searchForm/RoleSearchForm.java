/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.global.webapp.models.searchForm;

import java.util.Set;

/**
 *
 * Created by ThuyetLV
 */
public class RoleSearchForm {

    public Integer limit;
    public Integer page;
    public String name;
    public String description;
    public String userName;

    public Set<Long> permissionIds;

}
