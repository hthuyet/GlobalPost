package com.global.webapp.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String login(HttpServletRequest request) {
//        String referrer = request.getHeader("Referer");
//        request.getSession().setAttribute("url_prior_login", referrer);
        return "login";
    }
}
