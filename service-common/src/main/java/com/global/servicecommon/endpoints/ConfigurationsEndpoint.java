package com.global.servicecommon.endpoints;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.global.servicecommon.model.Configuration;
import com.global.servicecommon.services.ConfigurationService;
import com.global.servicecommon.core.SsdcCrudEndpoint;

import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Component
@Path("configuration")
@Api("Common")
@Produces(APPLICATION_JSON)
public class ConfigurationsEndpoint extends SsdcCrudEndpoint<String, Configuration> {

    private ConfigurationService configurationService;

    @Autowired
    public ConfigurationsEndpoint(ConfigurationService configurationService) {
        this.service = this.configurationService = configurationService;
    }

}
