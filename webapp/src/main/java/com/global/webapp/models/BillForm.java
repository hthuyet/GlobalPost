package com.global.webapp.models;

import com.global.webapp.utils.Utils;

import java.util.Date;
import java.util.Map;

public class BillForm {
    public Long id;
    public String billNo;
    public int billType;
    public Long weight;
    public Long cost;
    public Long totalCost;
    public String content;
    public int paid;
    public Long created;
    public Long updated;
    public int isCod;
    public Long codValue;
    public int billState;
    public int whoPay;
    public Long userCreate;
    public Long branchCreate;
    public Long currentBranch;
    public Long partnerId;
    public Long employeeSend;
    public Long employeeReceive;
    public Long sendCustomer;
    public String sendName;
    public String sendAddress;
    public String sendMobile;
    public String sendTime;
    public String sendDate;
    public String sendBy;
    public Long receiveCustomer;
    public String receiveName;
    public String receiveAddress;
    public String receiveMobile;
    public String receiveTime;
    public String receiveDate;
    public String receiveBy;
    public int saveSender;
    public int saveReceiver;
    public int payType;

    public BillForm(Map<String, String> params) {
        id = Utils.getAsLong(params, "id", null);
        billNo = Utils.getAsString(params, "billNo", "");
        billType = Utils.getAsInt(params, "billType", 0);
        weight = Utils.getAsLong(params, "weight", null);
        cost = Utils.getAsLong(params, "cost", null);
        totalCost = Utils.getAsLong(params, "totalCost", null);
        content = Utils.getAsString(params, "content", null);
        paid = Utils.getAsInt(params, "paid", 0);
        isCod = Utils.getAsInt(params, "isCod", 0);
        codValue = Utils.getAsLong(params, "codValue", null);
        billState = Utils.getAsInt(params, "billState", 0);
        whoPay = Utils.getAsInt(params, "whoPay", 0);
        userCreate = Utils.getAsLong(params, "userCreate", 0L);
        branchCreate = Utils.getAsLong(params, "branchCreate", 0L);
        currentBranch = Utils.getAsLong(params, "currentBranch", 0L);
        partnerId = Utils.getAsLong(params, "partnerId", null);
        employeeSend = Utils.getAsLong(params, "employeeSend", null);
        employeeReceive = Utils.getAsLong(params, "employeeReceive", null);
        sendCustomer = Utils.getAsLong(params, "sendCustomer", null);
        sendName = Utils.getAsString(params, "sendName", "");
        sendAddress = Utils.getAsString(params, "sendAddress", "");
        sendMobile = Utils.getAsString(params, "sendMobile", "");
        sendTime = Utils.getAsString(params, "sendTime", "");
        sendDate = Utils.getAsString(params, "sendDate", "");
        sendBy = Utils.getAsString(params, "sendBy", "");
        receiveCustomer = Utils.getAsLong(params, "receiveCustomer", null);
        receiveName = Utils.getAsString(params, "receiveName", "");
        receiveAddress = Utils.getAsString(params, "receiveAddress", "");
        receiveMobile = Utils.getAsString(params, "receiveMobile", "");
        receiveTime = Utils.getAsString(params, "receiveTime", "");
        receiveDate = Utils.getAsString(params, "receiveDate", "");
        receiveBy = Utils.getAsString(params, "receiveBy", "");
        saveSender = Utils.getAsInt(params, "saveSender", 0);
        saveReceiver = Utils.getAsInt(params, "saveReceiver", 0);
        payType = Utils.getAsInt(params, "payType", 0);

        long currentTime = new Date().getTime();
        if (id == null || id <= 0) {
            //Create
            created = currentTime;
            updated = currentTime;
        } else {
            //Update
            updated = currentTime;
        }
    }

    public BillForm() {

    }
}
