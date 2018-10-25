package com.global.servicebase.endpoints;

import com.global.core.SsdcCrudEndpoint;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.global.servicebase.model.EmailTemplate;
import com.global.servicebase.services.EmailTemplateService;
import javax.ws.rs.*;
import java.util.List;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

/**
 * Created by ThuyetLV
 */
@Component
@Path("email-template")
@Api("EmailTemplate")
@Produces(APPLICATION_JSON)
public class EmailTemplateEndPoint extends SsdcCrudEndpoint<String, EmailTemplate> {

    private EmailTemplateService emailTemplateService;

    @Autowired
    public EmailTemplateEndPoint(EmailTemplateService emailTemplateService) {
        this.service = this.emailTemplateService = emailTemplateService;
    }

    @GET
    @Path("/search-email")
    public List<EmailTemplate> searchRole(
            @ApiParam(value = "Number of returned devices, default is 50") @DefaultValue("20") @QueryParam("limit") String limit,
            @ApiParam(value = "Starting index of the returned list, default is 0") @DefaultValue("0") @QueryParam("indexPage") String indexPage) {
        return this.emailTemplateService.searchEmail(limit, indexPage);
    }
}
