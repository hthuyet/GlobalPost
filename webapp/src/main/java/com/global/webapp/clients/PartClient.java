package com.global.webapp.clients;

import com.global.webapp.models.User;
import com.global.webapp.models.searchForm.UserSearchForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * Created by ThuyetLV
 */
@Component
public class PartClient {

    private RestTemplate restTemplate;
    private String endpointUrl;
    private HttpSession session;

    @Autowired
    public PartClient(RestTemplate restTemplate,
                      @Value("${apiEndpointUrl}") String identityServiceEndpointUrl) {
        this.restTemplate = restTemplate;
        this.endpointUrl = identityServiceEndpointUrl + "/part";
    }

    public String delete(String userName) {
        String url = String.format("%s/%s", this.endpointUrl, userName);
        this.restTemplate.delete(url);
        return "200";
    }

    public String search(Map<String,String> maps) {
        String url = String.format("%s/search", this.endpointUrl);
        return this.restTemplate.postForObject(url, maps, String.class);
    }

    public int count(Map<String,String> maps) {
        String url = String.format("%s/count", this.endpointUrl);
        return this.restTemplate.postForObject(url, maps, Integer.class);
    }

    public String get(Long id) {
        String url = String.format("%s/%s", this.endpointUrl, id.toString());
        return this.restTemplate.getForObject(url, String.class);
    }

    public String save(Map<String,String> maps) {
        return this.restTemplate.postForObject(this.endpointUrl, maps, String.class);
    }
}
