/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.vnpt.ssdc.user.endpoints;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.TEXT_PLAIN;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import vn.vnpt.ssdc.core.SsdcCrudEndpoint;
import vn.vnpt.ssdc.user.model.RoleSearchForm;
import vn.vnpt.ssdc.user.model.Role;
import vn.vnpt.ssdc.user.services.RoleService;

/**
 *
 * @author Admin
 */
@Component
@Path("/roles")
@Produces(APPLICATION_JSON)
@Consumes({APPLICATION_JSON, TEXT_PLAIN})
@Api("Roles")
public class RoleEndPoint  extends SsdcCrudEndpoint<Long, Role> {

    private RoleService roleService;

    @Autowired
    public RoleEndPoint(RoleService roleService) {
        this.service = this.roleService = roleService;
    }

    @POST
    @Path("/check-exist-name")
    @ApiOperation(value = "check exist role by name")
    @ApiResponse(code = 200, message = "Success", response = Role.class)
    public List<Role> checkExistName(@RequestBody RoleSearchForm searchParameter) throws Exception {
        return roleService.getByName(searchParameter);
    }

    /**
     * Search roless
     *
     * @param searchParameter
     * @return
     */
    @POST
    @Path("/search")
    @ApiOperation(value = "Search Roles")
    @ApiResponse(code = 200, message = "Success")
    public List<Role> search(@RequestBody RoleSearchForm searchParameter) {
        return roleService.search(searchParameter);
    }

    @POST
    @Path("/count")
    @ApiOperation(value = "Count Roles")
    @ApiResponse(code = 200, message = "Success")
    public int count(@RequestBody RoleSearchForm searchParameter) {
        return roleService.count(searchParameter);
    }
}
