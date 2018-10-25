/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.global.servicebase.user.endpoints;

import com.global.core.SsdcCrudEndpoint;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.TEXT_PLAIN;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import com.global.servicebase.user.model.PermissionSearchForm;
import com.global.servicebase.user.model.Permission;
import com.global.servicebase.user.services.PermissionsService;

/**
 *
 * Created by ThuyetLV
 */
@Component
@Path("/permissions")
@Produces(APPLICATION_JSON)
@Consumes({APPLICATION_JSON, TEXT_PLAIN})
@Api("Permissions")
public class PermissionEndPoint extends SsdcCrudEndpoint<Long, Permission> {

    private Logger logger = LoggerFactory.getLogger(PermissionEndPoint.class);

    private PermissionsService permissionsService;

    @Autowired
    public PermissionEndPoint(PermissionsService permissionsService) {
        this.service = this.permissionsService = permissionsService;
    }

    @POST
    @Path("/check-exist-name")
    @ApiOperation(value = "check exist permission by name")
    @ApiResponse(code = 200, message = "Success", response = Permission.class)
    public List<Permission> checkExistName(@RequestBody PermissionSearchForm searchParameter) throws Exception {
        return permissionsService.getByName(searchParameter);
    }

    /**
     * Search permissionss
     *
     * @param searchParameter
     * @return
     */
    @POST
    @Path("/search")
    @ApiOperation(value = "Search Permissions")
    @ApiResponse(code = 200, message = "Success")
    public List<Permission> search(@RequestBody PermissionSearchForm searchParameter) {
        return permissionsService.search(searchParameter);
    }

    @POST
    @Path("/count")
    @ApiOperation(value = "Count Permissions")
    @ApiResponse(code = 200, message = "Success")
    public int count(@RequestBody PermissionSearchForm searchParameter) {
        return permissionsService.count(searchParameter);
    }

    @GET
    @Path("/test")
    @ApiOperation(value = "Test Permissions")
    @ApiResponse(code = 200, message = "Success")
    public Boolean test() {
        permissionsService.afterUpdate(null, null);
        return true;
    }
}
