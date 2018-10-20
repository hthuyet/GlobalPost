package com.global.webapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import com.global.webapp.clients.UserClient;
import com.global.webapp.models.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by ThuyetLV
 */
////@Component
//public class UmpAuthSuccessHandler implements AuthenticationSuccessHandler {
public class UmpAuthSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Autowired
    private HttpSession session;

    @Autowired
    private UserClient userClient;

    public UmpAuthSuccessHandler(String defaultTargetUrl) {
        setDefaultTargetUrl(defaultTargetUrl);
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        Set<String> roles = new HashSet<String>();
        for (GrantedAuthority authority : authentication.getAuthorities()) {
            roles.add(authority.getAuthority());
        }
        session.setAttribute("roles", roles);
        session.setAttribute("username", authentication.getName());
        User user = userClient.getByUsername(authentication.getName());
        session.setAttribute("userId", user.userId);

        HttpSession session = httpServletRequest.getSession();
        if (session != null) {
            String redirectUrl = (String) session.getAttribute("url_prior_login");
            if (redirectUrl != null) {
                // we do not forget to clean this attribute from session
                session.removeAttribute("url_prior_login");
                // then we redirect
                getRedirectStrategy().sendRedirect(httpServletRequest, httpServletResponse, redirectUrl);
            } else {
                super.onAuthenticationSuccess(httpServletRequest, httpServletResponse, authentication);
            }
        } else {

            httpServletResponse.sendRedirect("/users/");
        }
    }
}
