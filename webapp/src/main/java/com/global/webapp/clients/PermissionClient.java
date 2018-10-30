package com.global.webapp.clients;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import com.global.webapp.models.Permissions;
import com.global.webapp.models.searchForm.PermissionSearchForm;

/**
 * Created by ThuyetLV
 */
@Component
public class PermissionClient {

    private RestTemplate restTemplate;
    private String endpointUrl;

    public PermissionClient(RestTemplate restTemplate, @Value("${commondEndpointUrl}") String apiEndpointUrl) {
        this.restTemplate = restTemplate;
        this.endpointUrl = apiEndpointUrl + "/permissions";
    }

    public Permissions[] search(PermissionSearchForm permissionSearchForm) {
        String url = String.format("%s/search", this.endpointUrl);
        return this.restTemplate.postForObject(url, permissionSearchForm, Permissions[].class);
    }

    public int count(PermissionSearchForm permissionSearchForm) {
        String url = String.format("%s/count", this.endpointUrl);
        return this.restTemplate.postForObject(url, permissionSearchForm, Integer.class);
    }

    public Permissions[] checkExistName(PermissionSearchForm permissionSearchForm) {
        String url = String.format("%s/check-exist-name", this.endpointUrl);
        return this.restTemplate.postForObject(url, permissionSearchForm, Permissions[].class);
    }

    public Permissions create(Permissions permissions) {
        return this.restTemplate.postForObject(this.endpointUrl, permissions, Permissions.class);
    }

    public void update(Permissions permissions, Long id) {
        String url = String.format("%s/%s", this.endpointUrl, id);
        this.restTemplate.put(url, permissions);
    }

    public void delete(Long id) {
        String url = String.format("%s/%s", this.endpointUrl, id);
        this.restTemplate.delete(url);
    }

    public Permissions getByID(Long id) {
        String url = String.format("%s/%s", this.endpointUrl, id);
        return this.restTemplate.getForObject(url, Permissions.class);
    }

    public Permissions[] findAll() {
        return this.restTemplate.getForObject(endpointUrl, Permissions[].class);
    }
}
