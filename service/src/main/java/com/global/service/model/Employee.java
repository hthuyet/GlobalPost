/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.global.service.model;

import com.global.jdbc.SsdcEntity;
import javax.persistence.*;

/**
 *
 * @author HungNT
 */
@Entity
public class Employee extends SsdcEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;

    @Column(name = "full_name")
    public String fullName;

    @Column(name = "mobile")
    public String mobile;

    @Column(name = "address")
    public String address;

    @Column(name = "branch_id")
    public Long branchId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Long getBranchId() {
        return branchId;
    }

    public void setBranchId(Long branchId) {
        this.branchId = branchId;
    }
}
