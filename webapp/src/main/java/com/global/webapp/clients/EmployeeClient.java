package com.global.webapp.clients;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpSession;
import java.nio.charset.Charset;
import java.util.Map;

/**
 * Created by ThuyetLV
 */
@Component
public class EmployeeClient {

    private RestTemplate restTemplate;
    private String endpointUrl;
    private HttpSession session;

    @Autowired
    public EmployeeClient(RestTemplate restTemplate,
                          @Value("${apiEndpointUrl}") String identityServiceEndpointUrl) {
        this.restTemplate = restTemplate;
        this.restTemplate.getMessageConverters()
                .add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));
        this.endpointUrl = identityServiceEndpointUrl + "/employee";
    }

    public String delete(String userName) {
        String url = String.format("%s/%s", this.endpointUrl, userName);
        this.restTemplate.delete(url);
        return "200";
    }

    public String search(Map<String, String> maps) {
        String url = String.format("%s/search", this.endpointUrl);
        return this.restTemplate.postForObject(url, maps, String.class);
    }

    public int count(Map<String, String> maps) {
        String url = String.format("%s/count", this.endpointUrl);
        return this.restTemplate.postForObject(url, maps, Integer.class);
    }

    public String get(Long id) {
        String url = String.format("%s/%s", this.endpointUrl, id.toString());
        return this.restTemplate.getForObject(url, String.class);
    }

    public String save(Map<String, String> maps) {
        String url = String.format("%s/save", this.endpointUrl);
        return this.restTemplate.postForObject(url, maps, String.class);
    }

    public String delete(Map<String, String> maps) {
        String url = String.format("%s/delete", this.endpointUrl);
        return this.restTemplate.postForObject(url, maps, String.class);
    }
}
