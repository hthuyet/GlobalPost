/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.global.service.model;

import com.global.jdbc.SsdcEntity;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 *
 * @author HungNT
 */
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
}
