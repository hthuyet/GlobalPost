package com.global.service;

import io.swagger.jaxrs.config.BeanConfig;
import io.swagger.jaxrs.listing.ApiListingResource;
import io.swagger.jaxrs.listing.SwaggerSerializers;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.reflections.Reflections;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.ws.rs.Path;
import java.util.Set;

/**
 * Created by vietnq on 10/21/16.
 */
@Component
public class GlobalResourceConfig extends ResourceConfig {

    private static final Logger logger = LoggerFactory.getLogger(GlobalResourceConfig.class);

    @Value("${spring.jersey.application-path:/}")
    private String apiPath;

    public GlobalResourceConfig() {
        registerEndpoints();
    }

    //find all class annotated with javax.ws.rs.Path and register
    private void registerEndpoints() {
        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .setUrls(ClasspathHelper.forPackage("com.global.service"))
                .setScanners(new TypeAnnotationsScanner()));

        Set<Class<?>> endpoints = reflections.getTypesAnnotatedWith(Path.class, true);

        register(MultiPartFeature.class);

        for (Class<?> endpoint : endpoints) {
            logger.info("Registering endpoint: {}", endpoint.getName());
            register(endpoint);
        }
    }

    @PostConstruct
    public void init() {
        this.configureSwagger();
    }

    private void configureSwagger() {
        this.register(ApiListingResource.class);
        this.register(SwaggerSerializers.class);

        BeanConfig config = new BeanConfig();
        config.setConfigId("Global-jersey");
        config.setTitle("GLOBAL Rest API");
        config.setDescription("Rest API for Global");
        config.setVersion("v1");
        config.setContact("Global");
        config.setSchemes(new String[]{"http", "https"});
        config.setBasePath(this.apiPath);
        config.setResourcePackage("com.global.service");
        config.setPrettyPrint(true);
        config.setScan(true);
    }
}
