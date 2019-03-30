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
public class ReportClient {

    private RestTemplate restTemplate;
    private String endpointUrl;
    private HttpSession session;

    @Autowired
    public ReportClient(RestTemplate restTemplate,
                        @Value("${apiEndpointUrl}") String identityServiceEndpointUrl) {
        this.restTemplate = restTemplate;
        this.restTemplate.getMessageConverters()
                .add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));
        this.endpointUrl = identityServiceEndpointUrl + "/bill";
    }

    public String report(Map<String, Object> maps) {
        String url = String.format("%s/report", this.endpointUrl);
        return this.restTemplate.postForObject(url, maps, String.class);
    }
}
