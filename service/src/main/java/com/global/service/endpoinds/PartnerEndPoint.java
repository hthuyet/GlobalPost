/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.global.service.endpoinds;

import com.global.exception.ObjNotFoundException;
import com.global.service.model.Partner;
import com.global.service.repository.PartnerRepo;
import com.global.service.services.PartnerService;
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
@Path("partner")
@Api("PartnerEndPoint")
@Produces(APPLICATION_JSON)
public class PartnerEndPoint {

    private static final Logger logger = LoggerFactory.getLogger(PartnerEndPoint.class);

    @Autowired
    public PartnerService partnerService;

    @Autowired
    public PartnerRepo partnerRepo;

    //<editor-fold defaultstate="collapsed" desc="search partner">
    @POST
    @ApiOperation(value = "Search partner")
    @ApiResponse(code = 200, message = "Success")
    @Path("/search")
    public Response search(@ApiParam(value = "Form data",
            examples
            = @Example(
                    value = @ExampleProperty("{\"search\": \"\",\"limit\": 20,\"page\": 1}"))) String formData) {
        JsonObject object = new Gson().fromJson(formData, JsonObject.class);
        String search = Utils.getAsString(object, "search", "");
        Integer limit = Utils.getAsInt(object, "limit", 20);
        Integer page = Utils.getAsInt(object, "page", 1);
        page = (page <= 1) ? 0 : (page - 1);
        List<Partner> list = partnerService.findByQuery(search, page * limit, limit);
        return Response.ok().entity(list).build();
    }//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Count partner">
    @POST
    @ApiOperation(value = "Count partner")
    @ApiResponse(code = 200, message = "Success")
    @Path("/count")
    public Response count(@ApiParam(value = "Form data", examples
            = @Example(value
                    = @ExampleProperty("{\"search\": \"\"}"))) String formData) {
        JsonObject object = new Gson().fromJson(formData, JsonObject.class);
        String search = Utils.getAsString(object, "search", "");
        BigInteger count = partnerService.countByQuery(search);
        return Response.ok().entity(count.intValue()).build();
    }//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="save partner">
    @POST
    @Path("/save")
    @ApiOperation(value = "save partner")
    @ApiResponse(code = 200, message = "success")
    public Response add(@ApiParam(value = "Form data", examples
            = @Example(value
                    = @ExampleProperty("{\"id\": 2,\"name\": \"\"}"))) String formData) {
        JsonObject json = new JsonObject();
        try {
            logger.info(String.format("add : %s", formData));
            JsonObject object = new Gson().fromJson(formData, JsonObject.class);
            Partner entity = convertToObj(object);

            entity = this.partnerService.save(entity);

            if (entity != null && entity.getId() > 0) {
                json.addProperty("status", ActionResult.SUCCESS_CODE);
                json.addProperty("message", ActionResult.SUCCESS);
                return Response.ok().entity(json.toString()).build();
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
    private Partner convertToObj(JsonObject object) throws ObjNotFoundException {
        Long id = Utils.getAsLong(object, "id", null);
        Partner obj = null;
        if (id != null && id > 0) {
            obj = this.partnerRepo.findOne(id);
            if (obj == null) {
                throw new ObjNotFoundException(ObjNotFoundException.MESSAGE);
            }
        } else {
            obj = new Partner();
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
    @ApiOperation(value = "Read partner")
    @ApiResponse(code = 200, message = "Success", response = Partner.class)
    public Response getById(@PathParam("id") Long id) {
        logger.info(String.format("getById : %s", id));
        Partner entity = this.partnerRepo.findOne(id);
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
    @Path("/{id}")
    public Response Deletes(@ApiParam(value = "Form data", examples
            = @Example(value
                    = @ExampleProperty("[{\"id\": 1},{\"id\": 2}]"))) String formData) {
        int count = 0;
        JsonArray array = new Gson().fromJson(formData, JsonArray.class);
        if (array != null && array.size() > 0) {
            long id = 0L;
            for (JsonElement ele : array) {
                try {
                    id = ele.getAsLong();
                    if (partnerService.delete(id)) {
                        count++;
                    }
                } catch (Exception ex) {
                    logger.error(String.format("ERROR delete {%d}", id), ex);
                }
            }
        }
        return Response.ok().entity(count == array.size()).build();
    }//</editor-fold>
}
