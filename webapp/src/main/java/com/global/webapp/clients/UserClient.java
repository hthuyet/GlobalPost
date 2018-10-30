package com.global.webapp.clients;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import com.global.webapp.models.User;
import com.global.webapp.models.searchForm.UserSearchForm;

import javax.servlet.http.HttpSession;

/**
 * Created by ThuyetLV
 */
@Component
public class UserClient {

    private RestTemplate restTemplate;
    private String endpointUrl;
    private HttpSession session;

    @Autowired
    public UserClient(RestTemplate restTemplate,
            @Value("${commondEndpointUrl}") String identityServiceEndpointUrl) {
        this.restTemplate = restTemplate;
        this.endpointUrl = identityServiceEndpointUrl + "/users";
    }

    public User getByUsername(String username) {
        String url = String.format("%s/%s", this.endpointUrl, username);
        return this.restTemplate.getForObject(url, User.class);
    }

    public String delete(String userName) {
        String url = String.format("%s/%s", this.endpointUrl, userName);
        this.restTemplate.delete(url);
        return "200";
    }

    public User[] search(UserSearchForm userSearchForm) {
        String url = String.format("%s/search", this.endpointUrl);
        return this.restTemplate.postForObject(url, userSearchForm, User[].class);
    }

    public int count(UserSearchForm userSearchForm) {
        String url = String.format("%s/count", this.endpointUrl);
        return this.restTemplate.postForObject(url, userSearchForm, Integer.class);
    }

    public User create(User user) {
        return this.restTemplate.postForObject(this.endpointUrl, user, User.class);
    }

    public void update(String userNameOld, User user) {
        String url = String.format("%s/%s", this.endpointUrl, userNameOld);
        this.restTemplate.put(url, user);
    }

    public void forgotPassword(UserSearchForm userSearchForm) {
        String url = String.format("%s/forgot-password", this.endpointUrl);
        this.restTemplate.postForObject(url, userSearchForm, Void.class);
    }

    public void checkCurrentPassword(UserSearchForm userSearchForm) {
        String url = String.format("%s/check-current-password", this.endpointUrl);
        this.restTemplate.postForObject(url, userSearchForm, Void.class);
    }

    public User changePassword(UserSearchForm userSearchForm) {
        String url = String.format("%s/change-password", this.endpointUrl);
        return this.restTemplate.postForObject(url, userSearchForm, User.class);
    }

    public Boolean forgotPasswordWithEmail(UserSearchForm userSearchForm) {
        return this.restTemplate.postForObject(String.format("%s/forgot-password", this.endpointUrl), userSearchForm, Boolean.class);
    }

    public Boolean changePasswordWithToken(UserSearchForm userSearchForm) {
        String url = String.format("%s/change-password-with-token", this.endpointUrl);
        return this.restTemplate.postForObject(url, userSearchForm, Boolean.class);
    }
}
