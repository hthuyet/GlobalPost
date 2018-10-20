/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.global.servicecommon.user.model;

import java.util.Set;

/**
 *
 * Created by ThuyetLV
 */
public class PermissionSearchForm {

    public Integer limit;
    public Integer page;
    public String name;
    public String groupName;
    public String description;

    public Set<String> operationIds;

}
