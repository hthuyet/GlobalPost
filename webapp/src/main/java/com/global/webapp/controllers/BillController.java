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
      logger.info("#USER_LOG {},{},{},{},{}", session.getId(), session.getAttribute("username"), "search customer on list bill page", "", "");
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

  @GetMapping("/bill/add")
  @PreAuthorize("hasAuthority('GLOBAL:BILL:CREATE')")
  public String add(Model model) {
    model.addAttribute("id", "");
    model.addAttribute("code", "");
    model.addAttribute("name", "");
    logger.info("#USER_LOG {},{},{},{},{}", session.getId(), session.getAttribute("username"), "go to add bill page", "", "");
    return BILL_PAGE_FORM;
  }

  @GetMapping("/bill/edit/{id}")
  @PreAuthorize("hasAuthority('GLOBAL:BILL:UPDATE')")
  public String edit(Model model, @PathVariable("id") String id) {
    JsonObject jsonObject = new Gson().fromJson(billClient.get(Long.parseLong(id)),JsonObject.class);
    System.out.println("------------" + Utils.getAsString(jsonObject,"code",""));
    model.addAttribute("id", Utils.getAsString(jsonObject,"id",""));
    model.addAttribute("code", Utils.getAsString(jsonObject,"code",""));
    model.addAttribute("name", Utils.getAsString(jsonObject,"name",""));
    model.addAttribute("address", Utils.getAsString(jsonObject,"address",""));
    model.addAttribute("taxCode", Utils.getAsString(jsonObject,"taxCode",""));
    model.addAttribute("taxAddress", Utils.getAsString(jsonObject,"taxAddress",""));
    model.addAttribute("mobile", Utils.getAsString(jsonObject,"mobile",""));
    model.addAttribute("email", Utils.getAsString(jsonObject,"email",""));
    model.addAttribute("note", Utils.getAsString(jsonObject,"note",""));
    model.addAttribute("userId", Utils.getAsString(jsonObject,"userId",""));
    logger.info("#USER_LOG {},{},{},{},{}", session.getId(), session.getAttribute("username"), "go to edit bill page", "", "");
    return BILL_PAGE_FORM;
  }

  @PostMapping("/bill/save")
  @PreAuthorize("hasAuthority('GLOBAL:BILL:CREATE')")
  @ResponseBody
  public ResponseEntity save(@RequestBody Map<String, String> params) {
    try {
      logger.info("#USER_LOG {},{},{},{},{}", session.getId(), session.getAttribute("username"), "execute bill customer", "", "");
      String rtn = billClient.save(params);
      return new ResponseEntity<>(rtn,HttpStatus.OK);
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
      String rtn = billClient.deteles(params.getOrDefault("ids",""));
      return new ResponseEntity<>(rtn,HttpStatus.OK);
    } catch (Exception ex) {
      return parseException(ex);
    }
  }
}
