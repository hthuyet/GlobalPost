package com.global.service.model.report;

public class EmployeeReport {
  public int stt;
  public String billNo;
  public String time;
  public String sendAddress;
  public String receiverAddress;
  public float weight;
  public float totalCost;
  public String notes;

  public EmployeeReport() {
  }

  public EmployeeReport(int stt) {
    this.stt = stt;
  }

  public EmployeeReport(int stt, String billNo, String time, String sendAddress, String receiverAddress, float weight, float totalCost, String notes) {
    this.stt = stt;
    this.billNo = billNo;
    this.time = time;
    this.sendAddress = sendAddress;
    this.receiverAddress = receiverAddress;
    this.weight = weight;
    this.totalCost = totalCost;
    this.notes = notes;
  }

  public int getStt() {
    return stt;
  }

  public void setStt(int stt) {
    this.stt = stt;
  }

  public String getBillNo() {
    return billNo;
  }

  public void setBillNo(String billNo) {
    this.billNo = billNo;
  }

  public String getTime() {
    return time;
  }

  public void setTime(String time) {
    this.time = time;
  }

  public String getSendAddress() {
    return sendAddress;
  }

  public void setSendAddress(String sendAddress) {
    this.sendAddress = sendAddress;
  }

  public String getReceiverAddress() {
    return receiverAddress;
  }

  public void setReceiverAddress(String receiverAddress) {
    this.receiverAddress = receiverAddress;
  }

  public float getWeight() {
    return weight;
  }

  public void setWeight(float weight) {
    this.weight = weight;
  }

  public float getTotalCost() {
    return totalCost;
  }

  public void setTotalCost(float totalCost) {
    this.totalCost = totalCost;
  }

  public String getNotes() {
    return notes;
  }

  public void setNotes(String notes) {
    this.notes = notes;
  }
}
