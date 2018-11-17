package com.global.webapp.clients;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import com.global.webapp.models.Role;
import com.global.webapp.models.searchForm.RoleSearchForm;

import java.nio.charset.Charset;

@Component
public class RoleClient {

    private RestTemplate restTemplate;
    private String endpointUrl;

    public RoleClient(RestTemplate restTemplate, @Value("${commondEndpointUrl}") String apiEndpointUrl) {
        this.restTemplate = restTemplate;
        this.restTemplate.getMessageConverters()
            .add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));
        this.endpointUrl = apiEndpointUrl + "/roles";
    }

    public Role[] search(RoleSearchForm roleSearchForm) {
        String url = String.format("%s/search", this.endpointUrl);
        return this.restTemplate.postForObject(url, roleSearchForm, Role[].class);
    }

    public int count(RoleSearchForm roleSearchForm) {
        String url = String.format("%s/count", this.endpointUrl);
        return this.restTemplate.postForObject(url, roleSearchForm, Integer.class);
    }

    public Role create(Role roles) {
        return this.restTemplate.postForObject(this.endpointUrl, roles, Role.class);
    }

    public void update(Role roles, Long id) {
        String url = String.format("%s/%s", this.endpointUrl, id);
        this.restTemplate.put(url, roles);
    }

    public void delete(Long id) {
        String url = String.format("%s/%s", this.endpointUrl, id);
        this.restTemplate.delete(url);
    }

    public Role[] search() {
        String url = String.format("%s", this.endpointUrl);
        return this.restTemplate.getForObject(url, Role[].class);
    }

    public Role get(String id) {
        String url = String.format("%s/%s", this.endpointUrl, id);
        return this.restTemplate.getForObject(url, Role.class);
    }

    public Role[] checkExistName(RoleSearchForm roleSearchForm) {
        String url = String.format("%s/check-exist-name", this.endpointUrl);
        return this.restTemplate.postForObject(url, roleSearchForm, Role[].class);
    }
}
