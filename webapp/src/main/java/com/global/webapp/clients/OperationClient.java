package com.global.webapp.clients;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import com.global.webapp.models.Operation;

/**
 * Created by ThuyetLV
 */
@Component
public class OperationClient {

    private RestTemplate restTemplate;
    private String endpointUrl;

    public OperationClient(RestTemplate restTemplate, @Value("${commondEndpointUrl}") String apiEndpointUrl) {
        this.restTemplate = restTemplate;
        this.endpointUrl = apiEndpointUrl + "/operations";
    }

    public Operation[] findAll() {
        return this.restTemplate.getForObject(endpointUrl, Operation[].class);
    }
}
