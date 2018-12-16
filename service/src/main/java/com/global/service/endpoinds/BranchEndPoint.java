/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.global.service.endpoinds;

import com.global.exception.ObjNotFoundException;
import com.global.service.model.Branch;
import com.global.service.repository.BranchRepo;
import com.global.service.services.BranchService;
import com.global.service.utils.ActionResult;
import com.global.service.utils.Utils;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.math.BigInteger;
import java.util.List;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

/**
 * Created by ThuyetLV
 */
@Component
@Path("branch")
@Api("BranchEndPoint")
@Produces(APPLICATION_JSON)
public class BranchEndPoint {

    private static final Logger logger = LoggerFactory.getLogger(BranchEndPoint.class);

    @Autowired
    public BranchService branchService;

    @Autowired
    public BranchRepo branchRepo;

    //<editor-fold defaultstate="collapsed" desc="search branch">
    @POST
    @ApiOperation(value = "Search branch")
    @ApiResponse(code = 200, message = "Success")
    @Path("/search")
    public Response search(@ApiParam(value = "Form data",
            examples
            = @Example(
                    value = @ExampleProperty("{\"search\": \"\",\"limit\": 20,\"page\": 1}"))) String formData) {
        JsonObject object = new Gson().fromJson(formData, JsonObject.class);
        String name = Utils.getAsString(object, "search", "");
        Integer limit = Utils.getAsInt(object, "limit", 20);
        Integer page = Utils.getAsInt(object, "page", 1);
        page = (page <= 1) ? 0 : (page - 1);
        List<Branch> list = branchService.findByQuery(name, page * limit, limit);
        return Response.ok().entity(list).build();
    }//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Count branch">
    @POST
    @ApiOperation(value = "Count branch")
    @ApiResponse(code = 200, message = "Success")
    @Path("/count")
    public Response count(@ApiParam(value = "Form data", examples
            = @Example(value
                    = @ExampleProperty("{\"search\": \"\"}"))) String formData) {
        JsonObject object = new Gson().fromJson(formData, JsonObject.class);
        String name = Utils.getAsString(object, "search", "");
        BigInteger count = branchService.countByQuery(name);
        return Response.ok().entity(count.intValue()).build();
    }//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="save">
    @POST
    @Path("/save")
    @ApiOperation(value = "save Branch")
    @ApiResponse(code = 200, message = "success")
    public Response save(@ApiParam(value = "Form data", examples
            = @Example(value
                    = @ExampleProperty("{\"id\": 2,\"name\": \"\",\"address\": \"\",\"hotline\": \"\"}"))) String formData) {
        JsonObject json = new JsonObject();
        try {
            logger.info(String.format("add : %s", formData));
            JsonObject object = new Gson().fromJson(formData, JsonObject.class);
            Branch entity = convertToObj(object);
            entity = this.branchService.save(entity);
            if (entity != null && entity.getId() > 0) {
                //Update Branch ok ==> Update User
                int result = branchService.saveBranchOfUser(entity.getName(), entity.getId());
                if (result > 0) {
                    json.addProperty("status", ActionResult.SUCCESS_CODE);
                    json.addProperty("message", ActionResult.SUCCESS);
                    return Response.ok().entity(json.toString()).build();
                } else {
                    json.addProperty("status", ActionResult.FAILURE1_CODE);
                    json.addProperty("message", "ERRROR");
                    return Response.ok().entity(json.toString()).build();
                }
            } else {
                json.addProperty("status", ActionResult.FAILURE1_CODE);
                json.addProperty("message", "ERRROR");
                return Response.serverError().entity(json.toString()).build();
            }
        } catch (ObjNotFoundException ex) {
            logger.error(String.format("ERROR ObjNotFoundException {%s}", formData), ex);
            json.addProperty("status", ActionResult.FAILURE1_CODE);
            json.addProperty("message", "ERRROR");
            return Response.serverError().entity(json.toString()).build();
        }

    }//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="convertToObj">
    private Branch convertToObj(JsonObject object) throws ObjNotFoundException {
        Long id = Utils.getAsLong(object, "id", null);
        Branch obj = null;
        if (id != null && id > 0) {
            obj = this.branchRepo.findOne(id);
            if (obj == null) {
                throw new ObjNotFoundException(ObjNotFoundException.MESSAGE);
            }
        } else {
            obj = new Branch();
        }
        obj.setId(Utils.getAsLong(object, "id", null));
        obj.setName(Utils.getAsString(object, "name", ""));
        obj.setAddress(Utils.getAsString(object, "address", ""));
        obj.setHotline(Utils.getAsString(object, "hotline", ""));
        return obj;
    }//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="get by id">
    @GET
    @Path("/{id}")
    @ApiOperation(value = "Read branch")
    @ApiResponse(code = 200, message = "Success", response = Branch.class)
    public Response getById(@PathParam("id") Long id) {
        logger.info(String.format("getById : %s", id));
        Branch entity = this.branchRepo.findOne(id);
        if (entity != null && entity.getId() > 0) {
            return Response.ok().entity(entity).build();
        } else {
            JsonObject json = new JsonObject();
            json.addProperty("status", ActionResult.FAILURE1_CODE);
            json.addProperty("message", "Branch not found.");
            return Response.serverError().entity(json.toString()).build();
        }

    }//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Delete">
    @POST
    @ApiOperation(value = "Delete")
    @ApiResponse(code = 200, message = "Success")
    @Path("/delete")
    public Response delete(@ApiParam(value = "Form data", examples
            = @Example(value
                    = @ExampleProperty("[1,2]"))) String formData) {
        JsonObject json = new JsonObject();
        try {
            int count = 0;
            JsonArray array = new Gson().fromJson(formData, JsonArray.class);
            if (array != null && array.size() > 0) {
                long id = 0L;
                for (JsonElement ele : array) {
                    try {
                        id = ele.getAsLong();
                        BigInteger numOfUsers = branchService.countUserByBranch(id);
                        if (numOfUsers.intValue() > 0) {
                        } else {
                            if (branchService.delete(id)) {
                                count++;
                            }
                        }
                    } catch (Exception ex) {
                        logger.error(String.format("ERROR delete {%d}", id), ex);
                    }
                }
            }
            json.addProperty("status", ActionResult.SUCCESS_CODE);
            json.addProperty("deleteok", count);
            json.addProperty("deletefail", array.size() - count);
            return Response.ok().entity(json.toString()).build();
        } catch (Exception ex) {
            logger.error(String.format("ERROR Exception {%s}", formData), ex);
            json.addProperty("status", ActionResult.FAILURE1_CODE);
            json.addProperty("message", "ERRROR");
            return Response.serverError().entity(json.toString()).build();
        }
    }//</editor-fold>
}
