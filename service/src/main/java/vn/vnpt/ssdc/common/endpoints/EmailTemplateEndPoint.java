package vn.vnpt.ssdc.common.endpoints;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import vn.vnpt.ssdc.common.model.EmailTemplate;
import vn.vnpt.ssdc.common.services.EmailTemplateService;
import vn.vnpt.ssdc.core.SsdcCrudEndpoint;

import javax.ws.rs.*;

import java.util.List;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

/**
 * Created by Lamborgini on 6/7/2017.
 */
@Component
@Path("email-template")
@Api("EmailTemplate")
@Produces(APPLICATION_JSON)
public class EmailTemplateEndPoint  extends SsdcCrudEndpoint<String, EmailTemplate> {

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
        return this.emailTemplateService.searchEmail(limit,indexPage);
    }
}
