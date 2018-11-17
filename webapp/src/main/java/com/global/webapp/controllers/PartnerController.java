package com.global.webapp.controllers;

import com.global.webapp.clients.PartnerClient;
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
public class PartnerController extends BaseController {
  private Logger logger = LoggerFactory.getLogger(PartnerController.class);

  @Autowired
  protected PartnerClient partnerClient;

  @Autowired
  protected HttpSession session;

  @Autowired
  HttpSession httpSession;

  @GetMapping("/partner")
  @PreAuthorize("hasAuthority('GLOBAL:PARTNER:READ')")
  public String index() {
    logger.info("#USER_LOG {},{},{},{},{}", session.getId(), session.getAttribute("username"), "go to partner page", "", "");
    return PARTNER_PAGE;
  }

  @PostMapping("/partner/search")
  @PreAuthorize("hasAuthority('GLOBAL:PARTNER:READ')")
  @ResponseBody
  public ResponseEntity search(@RequestBody Map<String, String> params) {
    try {
      logger.info("#USER_LOG {},{},{},{},{}", session.getId(), session.getAttribute("username"), "search partner on list partner page", "", "");
      String rtn = partnerClient.search(params);
      return new ResponseEntity<>(rtn, HttpStatus.OK);
    } catch (Exception ex) {
      return parseException(ex);
    }
  }

  @PostMapping("/partner/count")
  @PreAuthorize("hasAuthority('GLOBAL:PARTNER:READ')")
  @ResponseBody
  public int countDevices(@RequestBody Map<String, String> params) {
    logger.info("#USER_LOG {},{},{},{},{}", session.getId(), session.getAttribute("username"), "count partner on list partner page", "", "");
    return partnerClient.count(params);
  }

  @GetMapping("/partner/add")
  @PreAuthorize("hasAuthority('GLOBAL:PARTNER:CREATE')")
  public String add(Model model) {
    logger.info("#USER_LOG {},{},{},{},{}", session.getId(), session.getAttribute("username"), "go to add partner page", "", "");
    model.addAttribute("data", "");
    return PARTNER_PAGE_FORM;
  }

  @GetMapping("partner/edit/{id}")
  @PreAuthorize("hasAuthority('GLOBAL:PARTNER:UPDATE')")
  public String edit(Model model, @PathVariable("id") String id) {
    String data = partnerClient.get(Long.parseLong(id));
    model.addAttribute("data", data);
    logger.info("#USER_LOG {},{},{},{},{}", session.getId(), session.getAttribute("username"), "go to edit partner page", "", "");
    return PARTNER_PAGE_FORM;
  }

  @PostMapping("/partner/save")
  @PreAuthorize("hasAuthority('GLOBAL:PARTNER:CREATE')")
  @ResponseBody
  public ResponseEntity save(@RequestBody Map<String, String> params) {
    try {
      logger.info("#USER_LOG {},{},{},{},{}", session.getId(), session.getAttribute("username"), "execute save partner", "", "");
      String rtn = partnerClient.save(params);
      return new ResponseEntity<>(rtn,HttpStatus.OK);
    } catch (Exception ex) {
      return parseException(ex);
    }
  }
}
