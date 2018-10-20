package vn.vnpt.ssdc.common.endpoints;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import vn.vnpt.ssdc.common.model.Configuration;
import vn.vnpt.ssdc.common.services.ConfigurationService;
import vn.vnpt.ssdc.core.SsdcCrudEndpoint;

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
