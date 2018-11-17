package com.global.webapp.controllers;

import com.global.webapp.clients.CustomerClient;
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
public class CustomerController extends BaseController {
  private Logger logger = LoggerFactory.getLogger(CustomerController.class);

  @Autowired
  protected CustomerClient customerClient;

  @Autowired
  protected HttpSession session;

  @Autowired
  HttpSession httpSession;

  @GetMapping("/customer")
  @PreAuthorize("hasAuthority('GLOBAL:CUSTOMER:READ')")
  public String index() {
    logger.info("#USER_LOG {},{},{},{},{}", session.getId(), session.getAttribute("username"), "go to customer page", "", "");
    return CUSTOMER_PAGE;
  }

  @PostMapping("/customer/search")
  @PreAuthorize("hasAuthority('GLOBAL:CUSTOMER:READ')")
  @ResponseBody
  public ResponseEntity search(@RequestBody Map<String, String> params) {
    try {
      logger.info("#USER_LOG {},{},{},{},{}", session.getId(), session.getAttribute("username"), "search customer on list customer page", "", "");
      String rtn = customerClient.search(params);
      return new ResponseEntity<>(rtn, HttpStatus.OK);
    } catch (Exception ex) {
      return parseException(ex);
    }
  }

  @PostMapping("/customer/count")
  @PreAuthorize("hasAuthority('GLOBAL:CUSTOMER:READ')")
  @ResponseBody
  public int countDevices(@RequestBody Map<String, String> params) {
    logger.info("#USER_LOG {},{},{},{},{}", session.getId(), session.getAttribute("username"), "count customer on list customer page", "", "");
    return customerClient.count(params);
  }

  @GetMapping("/customer/add")
  //@PreAuthorize("hasAuthority('GLOBAL:CUSTOMER:CREATE')")
  public String add(Model model) {
    model.addAttribute("id", "");
    model.addAttribute("code", "");
    model.addAttribute("name", "");
    logger.info("#USER_LOG {},{},{},{},{}", session.getId(), session.getAttribute("username"), "go to add customer page", "", "");
    return CUSTOMER_PAGE_FORM;
  }

  @GetMapping("/customer/edit/{id}")
  @PreAuthorize("hasAuthority('GLOBAL:CUSTOMER:UPDATE')")
  public String edit(Model model, @PathVariable("id") String id) {
    JsonObject jsonObject = new Gson().fromJson(customerClient.get(Long.parseLong(id)),JsonObject.class);
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
    logger.info("#USER_LOG {},{},{},{},{}", session.getId(), session.getAttribute("username"), "go to edit customer page", "", "");
    return CUSTOMER_PAGE_FORM;
  }

  @PostMapping("/customer/save")
//  @PreAuthorize("hasAuthority('GLOBAL:CUSTOMER:CREATE')")
  @ResponseBody
  public ResponseEntity save(@RequestBody Map<String, String> params) {
    try {
      logger.info("#USER_LOG {},{},{},{},{}", session.getId(), session.getAttribute("username"), "execute save customer", "", "");
      String rtn = customerClient.save(params);
      return new ResponseEntity<>(rtn,HttpStatus.OK);
    } catch (Exception ex) {
      return parseException(ex);
    }
  }


  @PostMapping("/customer/delete")
//  @PreAuthorize("hasAuthority('GLOBAL:CUSTOMER:DELETE')")
  @ResponseBody
  public ResponseEntity delete(@RequestBody Map<String, String> params) {
    try {
      logger.info("#USER_LOG {},{},{},{},{}", session.getId(), session.getAttribute("username"), "execute delete customer", "", "");
      String rtn = customerClient.deteles(params.getOrDefault("ids",""));
      return new ResponseEntity<>(rtn,HttpStatus.OK);
    } catch (Exception ex) {
      return parseException(ex);
    }
  }
}
