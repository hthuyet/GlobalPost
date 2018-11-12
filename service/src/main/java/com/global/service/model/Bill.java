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
public class Bill extends SsdcEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;

    @Column(name = "bill_no")
    public String billNo;

    @Column(name = "bill_type")
    public int billType;

    @Column(name = "weight")
    public Long weight;

    @Column(name = "cost")
    public Long cost;

    @Column(name = "total_cost")
    public Long totalCost;

    @Column(name = "content")
    public String content;

    @Column(name = "paid")
    public int paid;

    @Column(name = "created")
    public Long created;

    @Column(name = "updated")
    public Long updated;

    @Column(name = "is_cod")
    public int isCod;

    @Column(name = "cod_value")
    public Long codValue;

    @Column(name = "bill_state")
    public int billState;

    @Column(name = "who_pay")
    public int whoPay;

    @Column(name = "user_create")
    public Long userCreate;

    @Column(name = "branch_create")
    public Long branchCreate;

    @Column(name = "current_branch")
    public Long currentBranch;

    @Column(name = "partner_id")
    public Long partnerId;

    @Column(name = "employee_send")
    public Long employeeSend;

    @Column(name = "employee_receive")
    public Long employeeReceive;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public int getBillType() {
        return billType;
    }

    public void setBillType(int billType) {
        this.billType = billType;
    }

    public Long getWeight() {
        return weight;
    }

    public void setWeight(Long weight) {
        this.weight = weight;
    }

    public Long getCost() {
        return cost;
    }

    public void setCost(Long cost) {
        this.cost = cost;
    }

    public Long getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(Long totalCost) {
        this.totalCost = totalCost;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getPaid() {
        return paid;
    }

    public void setPaid(int paid) {
        this.paid = paid;
    }

    public int getIsCod() {
        return isCod;
    }

    public void setIsCod(int isCod) {
        this.isCod = isCod;
    }

    public Long getCodValue() {
        return codValue;
    }

    public void setCodValue(Long codValue) {
        this.codValue = codValue;
    }

    public int getBillState() {
        return billState;
    }

    public void setBillState(int billState) {
        this.billState = billState;
    }

    public int getWhoPay() {
        return whoPay;
    }

    public void setWhoPay(int whoPay) {
        this.whoPay = whoPay;
    }

    public Long getUserCreate() {
        return userCreate;
    }

    public void setUserCreate(Long userCreate) {
        this.userCreate = userCreate;
    }

    public Long getBranchCreate() {
        return branchCreate;
    }

    public void setBranchCreate(Long branchCreate) {
        this.branchCreate = branchCreate;
    }

    public Long getCurrentBranch() {
        return currentBranch;
    }

    public void setCurrentBranch(Long currentBranch) {
        this.currentBranch = currentBranch;
    }

    public Long getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(Long partnerId) {
        this.partnerId = partnerId;
    }

    public Long getEmployeeSend() {
        return employeeSend;
    }

    public void setEmployeeSend(Long employeeSend) {
        this.employeeSend = employeeSend;
    }

    public Long getEmployeeReceive() {
        return employeeReceive;
    }

    public void setEmployeeReceive(Long employeeReceive) {
        this.employeeReceive = employeeReceive;
    }
}
