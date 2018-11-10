/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.global.service.endpoinds;

import com.global.service.model.BillStock;
import com.global.service.model.BillStockForm;
import com.global.service.model.BillStockResponse;
import com.global.service.repository.BillRepo;
import com.global.service.services.BillStockService;
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
@Path("stock")
@Api("BillStockEndPoint")
@Produces(APPLICATION_JSON)
public class BillStockEndPoint {

    private static final Logger logger = LoggerFactory.getLogger(BranchEndPoint.class);

    @Autowired
    public BillStockService billStockService;

    @Autowired
    public BillRepo billRepo;

    @POST
    @ApiOperation(value = "Search Bill Stock")
    @ApiResponse(code = 200, message = "Success")
    @Path("/search")
    public Response search(@ApiParam(value = "Form data",
            examples
            = @Example(
                    value = @ExampleProperty("{\"billno\": \"\",\"state\": 0,\"from\": 0,\"to\": 0,\"limit\": 20,\"page\": 1}"))) String formData) {
        JsonObject object = new Gson().fromJson(formData, JsonObject.class);
        String billNo = Utils.getAsString(object, "billno", "");
        Long from = Utils.getAsLong(object, "from", 0L);
        Long to = Utils.getAsLong(object, "to", 0L);
        int state = Utils.getAsInt(object, "state", 0);
        Integer limit = Utils.getAsInt(object, "limit", 20);
        Integer page = Utils.getAsInt(object, "page", 1);
        page = (page <= 1) ? 0 : (page - 1);
        List<BillStockResponse> list = billStockService.findByQuery(billNo, state, from, to, page * limit, limit);
        return Response.ok().entity(list).build();
    }

    @POST
    @ApiOperation(value = "Count Bill in Stock")
    @ApiResponse(code = 200, message = "Success")
    @Path("/count")
    public Response count(@ApiParam(value = "Form data", examples
            = @Example(
                    value = @ExampleProperty("{\"billno\": \"\",\"state\": 0,\"from\": 0,\"to\": 0}"))) String formData) {
        JsonObject object = new Gson().fromJson(formData, JsonObject.class);
        String billNo = Utils.getAsString(object, "billno", "");
        Long from = Utils.getAsLong(object, "from", 0L);
        Long to = Utils.getAsLong(object, "to", 0L);
        int state = Utils.getAsInt(object, "state", 0);
        BigInteger count = billStockService.countByQuery(billNo, state, from, to);
        return Response.ok().entity(count.intValue()).build();
    }

    @GET
    @Path("/{id}")
    @ApiOperation(value = "Read Bill in Stock")
    @ApiResponse(code = 200, message = "Success", response = BillStockResponse.class)
    public Response getById(@PathParam("id") Long id) {
        try {
            logger.info(String.format("getById : %s", id));
            BillStockResponse entity = billStockService.getById(id);
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
    @ApiOperation(value = "Get Bill in Stock by Code")
    @ApiResponse(code = 200, message = "Success", response = BillStockResponse.class)
    public Response getByCode(@PathParam("code") String code) {
        try {
            logger.info(String.format("getByCode : %s", code));
            BillStockResponse entity = billStockService.getByCode(code);
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
        int count = 0;
        JsonArray array = new Gson().fromJson(formData, JsonArray.class);
        if (array != null && array.size() > 0) {
            long id = 0L;
            for (JsonElement item : array) {
                try {
                    id = item.getAsLong();
                    if (billStockService.delete(id)) {
                        count++;
                    }
                } catch (Exception ex) {
                    logger.error(String.format("ERROR delete {%d}", id), ex);
                }
            }
        }
        return Response.ok().entity(count == array.size()).build();
    }

    @POST
    @Path("/save")
    @ApiOperation(value = "Save Bill")
    @ApiResponse(code = 200, message = "success")
    public BillStock save(@RequestBody BillStockForm billStockParameter) {
        return billStockService.save(billStockParameter);
    }
}
