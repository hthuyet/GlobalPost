package com.global.webapp.controllers;

import com.global.webapp.clients.BranchClient;
import com.global.webapp.clients.EmployeeClient;
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
public class EmployeeController extends BaseController {
    private Logger logger = LoggerFactory.getLogger(EmployeeController.class);

    @Autowired
    protected EmployeeClient employeeClient;

    @Autowired
    protected HttpSession session;

    @Autowired
    HttpSession httpSession;

    @GetMapping("/employee")
//    @PreAuthorize("hasAuthority('GLOBAL:EMPLOYEE:READ')")
    public String index() {
        logger.info("#USER_LOG {},{},{},{},{}", session.getId(), session.getAttribute("username"), "go to Employee page", "", "");
        return EMPLOYEE_PAGE;
    }

    @PostMapping("/employee/search")
//    @PreAuthorize("hasAuthority('GLOBAL:EMPLOYEE:READ')")
    @ResponseBody
    public ResponseEntity search(@RequestBody Map<String, String> params) {
        try {
            logger.info("#USER_LOG {},{},{},{},{}", session.getId(), session.getAttribute("username"), "search on list Employee page", "", "");
            String rtn = employeeClient.search(params);
            return new ResponseEntity<>(rtn, HttpStatus.OK);
        } catch (Exception ex) {
            return parseException(ex);
        }
    }

    @PostMapping("/employee/count")
//    @PreAuthorize("hasAuthority('GLOBAL:EMPLOYEE:READ')")
    @ResponseBody
    public int countDevices(@RequestBody Map<String, String> params) {
        logger.info("#USER_LOG {},{},{},{},{}", session.getId(), session.getAttribute("username"), "count on list Employee page", "", "");
        return employeeClient.count(params);
    }

    @GetMapping("/employee/add")
//    @PreAuthorize("hasAuthority('GLOBAL:EMPLOYEE:CREATE')")
    public String add(Model model) {
        logger.info("#USER_LOG {},{},{},{},{}", session.getId(), session.getAttribute("username"), "go to add Employee page", "", "");
        model.addAttribute("data", "");
        return EMPLOYEE_PAGE_FORM;
    }

    @GetMapping("employee/edit/{id}")
//    @PreAuthorize("hasAuthority('GLOBAL:EMPLOYEE:UPDATE')")
    public String edit(Model model, @PathVariable("id") String id) {
        logger.info("#USER_LOG {},{},{},{},{}", session.getId(), session.getAttribute("username"), "go to edit Employee page", "", "");
        String data = employeeClient.get(Long.parseLong(id));
        model.addAttribute("data", data);
        return EMPLOYEE_PAGE_FORM;
    }

    @PostMapping("/employee/save")
//    @PreAuthorize("hasAuthority('GLOBAL:EMPLOYEE:CREATE')")
    @ResponseBody
    public ResponseEntity save(@RequestBody Map<String, String> params) {
        try {
            logger.info("#USER_LOG {},{},{},{},{}", session.getId(), session.getAttribute("username"), "execute save Employee", "", "");
            String rtn = employeeClient.save(params);
            return new ResponseEntity<>(rtn, HttpStatus.OK);
        } catch (Exception ex) {
            return parseException(ex);
        }
    }
}
