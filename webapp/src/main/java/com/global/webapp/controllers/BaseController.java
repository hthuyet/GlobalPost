package com.global.webapp.controllers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.UnknownHttpStatusCodeException;

@Controller
public class BaseController {
    private Logger logger = LoggerFactory.getLogger(BaseController.class);

    public static final String BRANCH_PAGE = "branch/index";
    public static final String BRANCH_PAGE_FORM = "branch/form";

    public static final String EMPLOYEE_PAGE = "employee/index";
    public static final String EMPLOYEE_PAGE_FORM = "employee/form";

    public static final String PARTNER_PAGE = "partner/index";
    public static final String PARTNER_PAGE_FORM = "partner/form";

    public static final String CUSTOMER_PAGE = "customer/index";
    public static final String CUSTOMER_PAGE_FORM = "customer/form";

    public static final String BILL_PAGE = "bill/index";
    public static final String BILL_IM_PAGE = "bill/import";
    public static final String BILL_EX_PAGE = "bill/export";
    public static final String BILL_PAGE_FORM = "bill/form";

    public ResponseEntity parseException(Exception ex) {
        JsonObject response = new JsonObject();
        if (ex instanceof UnknownHttpStatusCodeException) {
            logger.error("ERROR UnknownHttpStatusCodeException: ", ex);
            UnknownHttpStatusCodeException uex = (UnknownHttpStatusCodeException) ex;
            if (uex.getRawStatusCode() == 601) {
                response.addProperty("message", uex.getResponseBodyAsString());
            } else {
                String s = uex.getResponseBodyAsString();
                if (StringUtils.isNotBlank(s)) {
                    response = new Gson().fromJson(s, JsonObject.class);
                } else {
                    response.addProperty("message", ex.getMessage());
                }
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response.toString());
        } else if (ex instanceof HttpServerErrorException) {
            logger.error("ERROR HttpServerErrorException: ", ex);
            HttpServerErrorException hex = (HttpServerErrorException) ex;
            String s = hex.getResponseBodyAsString();
            if (StringUtils.isNotBlank(s)) {
                response = new Gson().fromJson(s, JsonObject.class);
            } else {
                response.addProperty("message", ex.getMessage());
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response.toString());
        } else {
            logger.error("ERROR parseException: ", ex);
            response.addProperty("message", ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response.toString());
        }

    }
}
