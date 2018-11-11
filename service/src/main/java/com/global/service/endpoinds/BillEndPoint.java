/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.global.service.endpoinds;

import com.global.service.model.Bill;
import com.global.service.model.BillForm;
import com.global.service.model.BillResponse;
import com.global.service.repository.BillRepo;
import com.global.service.services.BillService;
import com.global.service.utils.ActionResult;
import com.global.service.utils.Utils;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.Example;
import io.swagger.annotations.ExampleProperty;
import java.math.BigInteger;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import javax.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

/**
 *
 * @author HungNT
 */
@Component
@Path("bill")
@Api("BillEndPoint")
@Produces(APPLICATION_JSON)
public class BillEndPoint {

    private static final Logger logger = LoggerFactory.getLogger(BranchEndPoint.class);

    @Autowired
    public BillService billService;

    @Autowired
    public BillRepo billRepo;

    @POST
    @ApiOperation(value = "Search Bill")
    @ApiResponse(code = 200, message = "Success")
    @Path("/search")
    public Response search(@ApiParam(value = "Form data",
            examples
            = @Example(
                    value = @ExampleProperty("{\"billno\": \"\",\"state\": 0,\"from\": 0,\"to\": 0,\"sname\": \"\",\"smobile\": \"\",\"rname\": \"\",\"rmobile\": \"\",\"limit\": 20,\"page\": 1}"))) String formData) {
        JsonObject object = new Gson().fromJson(formData, JsonObject.class);
        String billNo = Utils.getAsString(object, "billno", "");
        Long from = Utils.getAsLong(object, "from", 0L);
        Long to = Utils.getAsLong(object, "to", 0L);
        int state = Utils.getAsInt(object, "state", 0);
        String sName = Utils.getAsString(object, "sname", "");
        String sMobile = Utils.getAsString(object, "smobile", "");
        String rName = Utils.getAsString(object, "rname", "");
        String rMobile = Utils.getAsString(object, "rmobile", "");
        Integer limit = Utils.getAsInt(object, "limit", 20);
        Integer page = Utils.getAsInt(object, "page", 1);
        page = (page <= 1) ? 0 : (page - 1);
        List<BillResponse> list = billService.findByQuery(billNo, state, from, to, sName, sMobile, rName, rMobile, page * limit, limit);
        return Response.ok().entity(list).build();
    }

    @POST
    @ApiOperation(value = "Count Bill")
    @ApiResponse(code = 200, message = "Success")
    @Path("/count")
    public Response count(@ApiParam(value = "Form data", examples
            = @Example(
                    value = @ExampleProperty("{\"billno\": \"\",\"state\": 0,\"from\": 0,\"to\": 0,\"sname\": \"\",\"smobile\": \"\",\"rname\": \"\",\"rmobile\": \"\"}"))) String formData) {
        JsonObject object = new Gson().fromJson(formData, JsonObject.class);
        String billNo = Utils.getAsString(object, "billno", "");
        Long from = Utils.getAsLong(object, "from", 0L);
        Long to = Utils.getAsLong(object, "to", 0L);
        int state = Utils.getAsInt(object, "state", 0);
        String sName = Utils.getAsString(object, "sname", "");
        String sMobile = Utils.getAsString(object, "smobile", "");
        String rName = Utils.getAsString(object, "rname", "");
        String rMobile = Utils.getAsString(object, "rmobile", "");
        BigInteger count = billService.countByQuery(billNo, state, from, to, sName, sMobile, rName, rMobile);
        return Response.ok().entity(count.intValue()).build();
    }

    @GET
    @Path("/{id}")
    @ApiOperation(value = "Read Bill")
    @ApiResponse(code = 200, message = "Success", response = BillResponse.class)
    public Response getById(@PathParam("id") Long id) {
        try {
            logger.info(String.format("getById : %s", id));
            BillResponse entity = billService.getById(id);
            if (entity != null && entity.getId() > 0) {
                return Response.ok().entity(entity).build();
            } else {
                JsonObject json = new JsonObject();
                json.addProperty("status", ActionResult.FAILURE1_CODE);
                json.addProperty("message", "Bill not found.");
                return Response.serverError().entity(json.toString()).build();
            }
        } catch (Exception ex) {
            JsonObject json = new JsonObject();
            json.addProperty("status", ActionResult.FAILURE1_CODE);
            json.addProperty("message", "Has error.");
            return Response.serverError().entity(json.toString()).build();
        }
    }

    @GET
    @Path("/code/{code}")
    @ApiOperation(value = "Get Bill by Code")
    @ApiResponse(code = 200, message = "Success", response = BillResponse.class)
    public Response getByCode(@PathParam("code") String code) {
        try {
            logger.info(String.format("getByCode : %s", code));
            BillResponse entity = billService.getByCode(code);
            if (entity != null && entity.getId() > 0) {
                return Response.ok().entity(entity).build();
            } else {
                JsonObject json = new JsonObject();
                json.addProperty("status", ActionResult.FAILURE1_CODE);
                json.addProperty("message", "Bill not found.");
                return Response.serverError().entity(json.toString()).build();
            }
        } catch (Exception ex) {
            JsonObject json = new JsonObject();
            json.addProperty("status", ActionResult.FAILURE1_CODE);
            json.addProperty("message", "Has error.");
            return Response.serverError().entity(json.toString()).build();
        }
    }

    @POST
    @ApiOperation(value = "Delete")
    @ApiResponse(code = 200, message = "Success")
    @Path("/{id}")
    public Response Deletes(@ApiParam(value = "Form data", examples
            = @Example(value
                    = @ExampleProperty("[1,2]"))) String formData) {
        JsonObject json = new JsonObject();
        int count = 0;
        JsonArray array = new Gson().fromJson(formData, JsonArray.class);
        if (array != null && array.size() > 0) {
            long id = 0L;
            for (JsonElement item : array) {
                try {
                    id = item.getAsLong();
                    if (billService.delete(id)) {
                        count++;
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
    }

    @POST
    @Path("/save")
    @ApiOperation(value = "Save Bill")
    @ApiResponse(code = 200, message = "success")
    public Bill save(@RequestBody BillForm billParameter) {
        return billService.save(billParameter);
    }
}