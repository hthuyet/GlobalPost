package com.global.servicebase;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Created by thuyetlv
 */
@SpringBootApplication(
        scanBasePackages = {"com.global.servicebase"},
        exclude = {}
)
public class CommonApi {

    public static void main(String[] args) {
        SpringApplication.run(CommonApi.class, args);
    }
}
