/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.vnpt.ssdc.user.model;

import java.util.Set;

/**
 *
 * @author kiendt
 */
public class RoleSearchForm {

    public Integer limit;
    public Integer page;
    public String name;
    public String description;
    public String userName;

    public Set<Long> permissionIds;

}
