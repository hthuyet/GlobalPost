package com.global.webapp.models;

public class UmpResponse {

    public String status;
    public Object data;
    public String message;

    public UmpResponse() {
        this.status = "success";
    }

    public void setError(String message) {
        this.status = "error";
        this.message = message;
    }

    public void setSuccess(Object data) {
        this.status = "success";
        this.data = data;
    }
}
