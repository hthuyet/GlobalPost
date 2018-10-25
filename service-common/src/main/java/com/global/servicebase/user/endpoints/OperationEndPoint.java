package com.global.servicebase.user.endpoints;

import com.global.core.SsdcCrudEndpoint;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.global.servicebase.user.services.OperationService;
import com.global.servicebase.user.model.Operation;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

/**
 * Created by ThuyetLV
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
