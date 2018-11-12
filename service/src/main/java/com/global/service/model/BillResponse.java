/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.global.service.model;

/**
 *
 * @author HungNT
 */
public class BillResponse {

    private Long id;
    private String billNo;
    private int billType;
    private Long weight;
    private Long cost;
    private Long totalCost;
    private String content;
    private int paid;
    private Long created;
    private Long updated;
    private int isCod;
    private Long codValue;
    private int billState;
    private int whoPay;
    private Long userCreate;
    private Long branchCreate;
    private Long currentBranch;
    private Long partnerId;
    private Long employeeSend;
    private Long employeeReceive;
    private Long sendCustomer;
    private String sendName;
    private String sendAddress;
    private String sendMobile;
    private String sendTime;
    private String sendDate;
    private String sendBy;
    private Long receiveCustomer;
    private String receiveName;
    private String receiveAddress;
    private String receiveMobile;
    private String receiveTime;
    private String receiveDate;
    private String receiveBy;
    private String userNameCreate;
    private String branchNameCreate;
    private String currentBranchName;
    private String partnerName;

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

    public Long getCreated() {
        return created;
    }

    public void setCreated(Long created) {
        this.created = created;
    }

    public Long getUpdated() {
        return updated;
    }

    public void setUpdated(Long updated) {
        this.updated = updated;
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

    public String getSendName() {
        return sendName;
    }

    public void setSendName(String sendName) {
        this.sendName = sendName;
    }

    public String getSendAddress() {
        return sendAddress;
    }

    public void setSendAddress(String sendAddress) {
        this.sendAddress = sendAddress;
    }

    public String getSendMobile() {
        return sendMobile;
    }

    public void setSendMobile(String sendMobile) {
        this.sendMobile = sendMobile;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public String getSendDate() {
        return sendDate;
    }

    public void setSendDate(String sendDate) {
        this.sendDate = sendDate;
    }

    public String getSendBy() {
        return sendBy;
    }

    public void setSendBy(String sendBy) {
        this.sendBy = sendBy;
    }

    public String getReceiveName() {
        return receiveName;
    }

    public void setReceiveName(String receiveName) {
        this.receiveName = receiveName;
    }

    public String getReceiveAddress() {
        return receiveAddress;
    }

    public void setReceiveAddress(String receiveAddress) {
        this.receiveAddress = receiveAddress;
    }

    public String getReceiveMobile() {
        return receiveMobile;
    }

    public void setReceiveMobile(String receiveMobile) {
        this.receiveMobile = receiveMobile;
    }

    public String getReceiveTime() {
        return receiveTime;
    }

    public void setReceiveTime(String receiveTime) {
        this.receiveTime = receiveTime;
    }

    public String getReceiveDate() {
        return receiveDate;
    }

    public void setReceiveDate(String receiveDate) {
        this.receiveDate = receiveDate;
    }

    public String getReceiveBy() {
        return receiveBy;
    }

    public void setReceiveBy(String receiveBy) {
        this.receiveBy = receiveBy;
    }

    public Long getSendCustomer() {
        return sendCustomer;
    }

    public void setSendCustomer(Long sendCustomer) {
        this.sendCustomer = sendCustomer;
    }

    public Long getReceiveCustomer() {
        return receiveCustomer;
    }

    public void setReceiveCustomer(Long receiveCustomer) {
        this.receiveCustomer = receiveCustomer;
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

    public String getUserNameCreate() {
        return userNameCreate;
    }

    public void setUserNameCreate(String userNameCreate) {
        this.userNameCreate = userNameCreate;
    }

    public String getBranchNameCreate() {
        return branchNameCreate;
    }

    public void setBranchNameCreate(String branchNameCreate) {
        this.branchNameCreate = branchNameCreate;
    }

    public String getCurrentBranchName() {
        return currentBranchName;
    }

    public void setCurrentBranchName(String currentBranchName) {
        this.currentBranchName = currentBranchName;
    }

    public Long getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(Long partnerId) {
        this.partnerId = partnerId;
    }

    public String getPartnerName() {
        return partnerName;
    }

    public void setPartnerName(String partnerName) {
        this.partnerName = partnerName;
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
