package com.global.webapp.controllers;

import com.global.webapp.clients.PartClient;
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
public class BranchController extends BaseController {
  private Logger logger = LoggerFactory.getLogger(BranchController.class);

  @Autowired
  protected PartClient partClient;

  @Autowired
  protected HttpSession session;

  @Autowired
  HttpSession httpSession;

  @GetMapping("/branch")
  @PreAuthorize("hasAuthority('GLOBAL:BRANCH:READ')")
  public String index() {
    logger.info("#USER_LOG {},{},{},{},{}", session.getId(), session.getAttribute("username"), "go to branch page", "", "");
    return BRANCH_PAGE;
  }

  @PostMapping("/branch/search")
  @PreAuthorize("hasAuthority('GLOBAL:BRANCH:READ')")
  @ResponseBody
  public ResponseEntity search(@RequestParam Map<String, String> params) {
    try {
      logger.info("#USER_LOG {},{},{},{},{}", session.getId(), session.getAttribute("username"), "search part on list branch page", "", "");
      String rtn = partClient.search(params);
      return new ResponseEntity<>(rtn, HttpStatus.OK);
    } catch (Exception ex) {
      return parseException(ex);
    }
  }

  @PostMapping("/branch/count")
  @PreAuthorize("hasAuthority('GLOBAL:BRANCH:READ')")
  @ResponseBody
  public int countDevices(@RequestParam Map<String, String> params) {
    logger.info("#USER_LOG {},{},{},{},{}", session.getId(), session.getAttribute("username"), "count part on list branch page", "", "");
    return partClient.count(params);
  }

  @GetMapping("/branch/add")
  @PreAuthorize("hasAuthority('GLOBAL:BRANCH:CREATE')")
  public String add() {
    logger.info("#USER_LOG {},{},{},{},{}", session.getId(), session.getAttribute("username"), "go to add branch page", "", "");
    return BRANCH_PAGE_FORM;
  }

  @GetMapping("branch/edit/{id}")
  @PreAuthorize("hasAuthority('GLOBAL:BRANCH:UPDATE')")
  public String edit(Model model, @PathVariable("id") String id) {
    model.addAttribute("role", partClient.get(Long.parseLong(id)));
    logger.info("#USER_LOG {},{},{},{},{}", session.getId(), session.getAttribute("username"), "go to edit branch page", "", "");
    return BRANCH_PAGE_FORM;
  }

  @PostMapping("/branch/save")
  @PreAuthorize("hasAuthority('GLOBAL:BRANCH:CREATE')")
  @ResponseBody
  public ResponseEntity save(@RequestBody Map<String, String> params) {
    try {
      logger.info("#USER_LOG {},{},{},{},{}", session.getId(), session.getAttribute("username"), "execute save branch", "", "");
      String rtn = partClient.save(params);
      return new ResponseEntity<>(rtn,HttpStatus.OK);
    } catch (Exception ex) {
      return parseException(ex);
    }
  }
}
