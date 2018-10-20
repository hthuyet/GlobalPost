package vn.vnpt.ssdc.user.endpoints;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import vn.vnpt.ssdc.user.services.OperationService;
import vn.vnpt.ssdc.user.model.Operation;
import vn.vnpt.ssdc.core.SsdcCrudEndpoint;

import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

/**
 * Created by Lamborgini on 5/9/2017.
 */
@Component
@Path("operations")
@Api("Operations")
@Produces(APPLICATION_JSON)
public class OperationEndPoint extends SsdcCrudEndpoint<String, Operation> {

    private OperationService operationService;

    @Autowired
    public OperationEndPoint(OperationService operationService) {
        this.service = this.operationService = operationService;
    }
}
