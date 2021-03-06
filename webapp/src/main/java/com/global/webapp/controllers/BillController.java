package com.global.webapp.controllers;

import com.global.webapp.clients.BillClient;
import com.global.webapp.utils.Utils;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Map;

@Controller
public class BillController extends BaseController {
  private Logger logger = LoggerFactory.getLogger(BillController.class);

  @Autowired
  protected BillClient billClient;

  @Autowired
  protected HttpSession session;

  @Autowired
  HttpSession httpSession;

  @GetMapping("/bill")
  @PreAuthorize("hasAuthority('GLOBAL:BILL:READ')")
  public String index() {
    logger.info("#USER_LOG {},{},{},{},{}", session.getId(), session.getAttribute("username"), "go to bill page", "", "");
    return BILL_PAGE;
  }

  @PostMapping("/bill/search")
  @PreAuthorize("hasAuthority('GLOBAL:BILL:READ')")
  @ResponseBody
  public ResponseEntity search(@RequestBody Map<String, String> params) {
    try {
      logger.info("#USER_LOG {},{},{},{},{}", session.getId(), session.getAttribute("username"), "search on list bill page", "", "");
      String rtn = billClient.search(params);
      return new ResponseEntity<>(rtn, HttpStatus.OK);
    } catch (Exception ex) {
      return parseException(ex);
    }
  }

  @PostMapping("/bill/count")
  @PreAuthorize("hasAuthority('GLOBAL:BILL:READ')")
  @ResponseBody
  public int countDevices(@RequestBody Map<String, String> params) {
    logger.info("#USER_LOG {},{},{},{},{}", session.getId(), session.getAttribute("username"), "count bill on list bill page", "", "");
    return billClient.count(params);
  }


  @PostMapping("/bill/searchImport")
  @PreAuthorize("hasAuthority('GLOBAL:BILL:READ')")
  @ResponseBody
  public ResponseEntity searchImport(@RequestBody Map<String, String> params) {
    try {
      logger.info("#USER_LOG {},{},{},{},{}", session.getId(), session.getAttribute("username"), "search import on list bill page", "", "");
      String rtn = billClient.searchImport(params);
      return new ResponseEntity<>(rtn, HttpStatus.OK);
    } catch (Exception ex) {
      return parseException(ex);
    }
  }

  @PostMapping("/bill/countImport")
  @PreAuthorize("hasAuthority('GLOBAL:BILL:READ')")
  @ResponseBody
  public int countImport(@RequestBody Map<String, String> params) {
    logger.info("#USER_LOG {},{},{},{},{}", session.getId(), session.getAttribute("username"), "count import on list bill page", "", "");
    return billClient.countImport(params);
  }

  @PostMapping("/bill/countExport")
  @PreAuthorize("hasAuthority('GLOBAL:BILL:READ')")
  @ResponseBody
  public int countExport(@RequestBody Map<String, String> params) {
    logger.info("#USER_LOG {},{},{},{},{}", session.getId(), session.getAttribute("username"), "count export on list bill page", "", "");
    return billClient.countExport(params);
  }

  @PostMapping("/bill/searchExport")
  @PreAuthorize("hasAuthority('GLOBAL:BILL:READ')")
  @ResponseBody
  public ResponseEntity searchExport(@RequestBody Map<String, String> params) {
    try {
      logger.info("#USER_LOG {},{},{},{},{}", session.getId(), session.getAttribute("username"), "search export on list bill page", "", "");
      String rtn = billClient.searchExport(params);
      return new ResponseEntity<>(rtn, HttpStatus.OK);
    } catch (Exception ex) {
      return parseException(ex);
    }
  }

  @GetMapping("/bill/add")
  @PreAuthorize("hasAuthority('GLOBAL:BILL:CREATE')")
  public String add(Model model) {
    model.addAttribute("id", "");
    model.addAttribute("code", "");
    model.addAttribute("name", "");
    logger.info("#USER_LOG {},{},{},{},{}", session.getId(), session.getAttribute("username"), "go to add bill page", "", "");
    return BILL_PAGE_FORM;
  }

  @PostMapping("/bill/detail/{code}")
  @PreAuthorize("hasAuthority('GLOBAL:BILL:UPDATE')")
  @ResponseBody
  public ResponseEntity detail(@PathVariable("code") String code) {
    try {
      JsonObject jsonObject = new Gson().fromJson(billClient.getByCode(code), JsonObject.class);
      return new ResponseEntity<>(jsonObject.toString(), HttpStatus.OK);
    } catch (Exception ex) {
      return parseException(ex);
    }
  }

  @GetMapping("/bill/edit/{id}")
  @PreAuthorize("hasAuthority('GLOBAL:BILL:UPDATE')")
  public String edit(Model model, @PathVariable("id") String id) {
    JsonObject jsonObject = new Gson().fromJson(billClient.get(Long.parseLong(id)), JsonObject.class);
    System.out.println("------------" + Utils.getAsString(jsonObject, "code", ""));
    model.addAttribute("id", Utils.getAsString(jsonObject, "id", ""));
    model.addAttribute("code", Utils.getAsString(jsonObject, "code", ""));
    model.addAttribute("name", Utils.getAsString(jsonObject, "name", ""));
    model.addAttribute("address", Utils.getAsString(jsonObject, "address", ""));
    model.addAttribute("taxCode", Utils.getAsString(jsonObject, "taxCode", ""));
    model.addAttribute("taxAddress", Utils.getAsString(jsonObject, "taxAddress", ""));
    model.addAttribute("mobile", Utils.getAsString(jsonObject, "mobile", ""));
    model.addAttribute("email", Utils.getAsString(jsonObject, "email", ""));
    model.addAttribute("note", Utils.getAsString(jsonObject, "note", ""));
    model.addAttribute("userId", Utils.getAsString(jsonObject, "userId", ""));
    model.addAttribute("employeeSend", Utils.getAsString(jsonObject, "employeeSend", ""));
    model.addAttribute("employeeReceive", Utils.getAsString(jsonObject, "employeeReceive", ""));
    model.addAttribute("data", jsonObject.toString());
    logger.info("#USER_LOG {},{},{},{},{}", session.getId(), session.getAttribute("username"), "go to edit bill page", "", "");
    return BILL_PAGE_FORM;
  }

  @PostMapping("/bill/save")
  @PreAuthorize("hasAuthority('GLOBAL:BILL:CREATE')")
  @ResponseBody
  public ResponseEntity save(@RequestBody Map<String, String> params) {
    try {
      logger.info("#USER_LOG {},{},{},{},{}", session.getId(), session.getAttribute("username"), "execute bill customer", "", "");
      params.put("userCreate", String.valueOf((Long) session.getAttribute("userId")));
      params.put("branchCreate", String.valueOf((Long) session.getAttribute("branchId")));
      String rtn = billClient.save(params);
      return new ResponseEntity<>(rtn, HttpStatus.OK);
    } catch (Exception ex) {
      return parseException(ex);
    }
  }


  @PostMapping("/bill/delete")
  @PreAuthorize("hasAuthority('GLOBAL:BILL:DELETE')")
  @ResponseBody
  public ResponseEntity delete(@RequestBody Map<String, String> params) {
    try {
      logger.info("#USER_LOG {},{},{},{},{}", session.getId(), session.getAttribute("username"), "execute bill customer", "", "");
      String rtn = billClient.deteles(params.getOrDefault("ids", ""));
      return new ResponseEntity<>(rtn, HttpStatus.OK);
    } catch (Exception ex) {
      return parseException(ex);
    }
  }

  @GetMapping("/bill/import")
//    @PreAuthorize("hasAuthority('GLOBAL:BILL:IMPORT')")
  public String importBill() {
    logger.info("#USER_LOG {},{},{},{},{}", session.getId(), session.getAttribute("username"), "go to bill import page", "", "");
    return BILL_IM_PAGE;
  }

  @PostMapping("/bill/exeImport")
//    @PreAuthorize("hasAuthority('GLOBAL:BILL:IMPORT')")
  @ResponseBody
  public ResponseEntity exeImport(@RequestBody Map<String, String> params) {
    try {
      logger.info("#USER_LOG {},{},{},{},{}", session.getId(), session.getAttribute("username"), "execute import bill", "", "");
      params.put("branchId", String.valueOf((Long) session.getAttribute("branchId")));
      String rtn = billClient.exeImport(params);
      return new ResponseEntity<>(rtn, HttpStatus.OK);
    } catch (Exception ex) {
      return parseException(ex);
    }
  }

  @GetMapping("/bill/export")
//    @PreAuthorize("hasAuthority('GLOBAL:BILL:EXPORT')")
  public String exportBill() {
    logger.info("#USER_LOG {},{},{},{},{}", session.getId(), session.getAttribute("username"), "go to bill export page", "", "");
    return BILL_EX_PAGE;
  }

  @PostMapping("/bill/exeExport")
//    @PreAuthorize("hasAuthority('GLOBAL:BILL:EXPORT')")
  @ResponseBody
  public ResponseEntity exeExport(@RequestBody Map<String, String> params) {
    try {
      logger.info("#USER_LOG {},{},{},{},{}", session.getId(), session.getAttribute("username"), "execute export bill", "", "");
      String rtn = billClient.exeExport(params);
      return new ResponseEntity<>(rtn, HttpStatus.OK);
    } catch (Exception ex) {
      return parseException(ex);
    }
  }
}
