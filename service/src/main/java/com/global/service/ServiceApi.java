package com.global.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Created by vietnq on 10/21/16.
 */
@SpringBootApplication(
        scanBasePackages = {"com.global.service"},
        exclude = {}
)
public class ServiceApi {

    public static void main(String[] args) {
        SpringApplication.run(ServiceApi.class, args);
    }
}
