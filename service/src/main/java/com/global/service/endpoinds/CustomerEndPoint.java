/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.global.service.endpoinds;

import com.global.exception.ObjExitsException;
import com.global.exception.ObjNotFoundException;
import com.global.service.model.Customer;
import com.global.service.repository.CustomerRepo;
import com.global.service.services.CustomerService;
import com.global.service.utils.ActionResult;
import com.global.service.utils.Utils;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.swagger.annotations.*;
import org.apache.commons.lang3.StringUtils;
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
@Path("customer")
@Api("CustomerEndPoint")
@Produces(APPLICATION_JSON)
public class CustomerEndPoint {

  private static final Logger logger = LoggerFactory.getLogger(CustomerEndPoint.class);

  @Autowired
  public CustomerService customerService;

  @Autowired
  public CustomerRepo customerRepo;

  //<editor-fold defaultstate="collapsed" desc="search customer">
  @POST
  @ApiOperation(value = "Search customer")
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
    List<Customer> list = customerService.findByQuery(search, page * limit, limit);
    return Response.ok().entity(list).build();
  }//</editor-fold>

  //<editor-fold defaultstate="collapsed" desc="Count customer">
  @POST
  @ApiOperation(value = "Count customer")
  @ApiResponse(code = 200, message = "Success")
  @Path("/count")
  public Response count(@ApiParam(value = "Form data", examples
      = @Example(value
      = @ExampleProperty("{\"search\": \"\"}"))) String formData) {
    JsonObject object = new Gson().fromJson(formData, JsonObject.class);
    String search = Utils.getAsString(object, "search", "");
    BigInteger count = customerService.countByQuery(search);
    return Response.ok().entity(count.intValue()).build();
  }//</editor-fold>

  //<editor-fold defaultstate="collapsed" desc="save customer">
  @POST
  @Path("/save")
  @ApiOperation(value = "save customer")
  @ApiResponse(code = 200, message = "success")
  public Response add(@ApiParam(value = "Form data", examples
      = @Example(value
      = @ExampleProperty("{\"id\": 2,\"name\": \"\"}"))) String formData) {
    JsonObject json = new JsonObject();
    try {
      Gson gson = new Gson();
      logger.info(String.format("add : %s", formData));
      JsonObject object = gson.fromJson(formData, JsonObject.class);
      Customer entity = convertToObj(object);

      //Check nhap code hay chua
      if (entity == null || StringUtils.isBlank(entity.getCode())) {
        json.addProperty("status", ActionResult.FAILURE1_CODE);
        json.addProperty("message", ActionResult.REQUEST_INVALID);
        return Response.serverError().entity(json.toString()).build();
      }

      entity = this.customerService.save(entity);

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
    } catch (ObjExitsException ex) {
      logger.error(String.format("ERROR ObjExitsException {%s}", formData), ex);
      json.addProperty("status", ActionResult.EXITS_CODE);
      json.addProperty("message", ex.getMessage());
      return Response.serverError().entity(json.toString()).build();
    }

  }//</editor-fold>

  //<editor-fold defaultstate="collapsed" desc="convertToObj">
  private Customer convertToObj(JsonObject object) throws ObjNotFoundException {
    Long id = Utils.getAsLong(object, "id", null);
    Customer obj = null;
    if (id != null && id > 0) {
      obj = this.customerRepo.findOne(id);
      if (obj == null) {
        throw new ObjNotFoundException(ObjNotFoundException.MESSAGE);
      }
    } else {
      obj = new Customer();
    }
    obj.setId(Utils.getAsLong(object, "id", null));
    obj.setName(Utils.getAsString(object, "name", ""));
    obj.setCode(Utils.getAsString(object, "code", ""));
    obj.setAddress(Utils.getAsString(object, "address", ""));
    obj.setEmail(Utils.getAsString(object, "email", ""));
    obj.setMobile(Utils.getAsString(object, "mobile", ""));
    obj.setTaxAddress(Utils.getAsString(object, "taxAddress", ""));
    obj.setTaxCode(Utils.getAsString(object, "taxCode", ""));
    obj.setNote(Utils.getAsString(object, "note", ""));
    obj.setUserId(Utils.getAsLong(object, "userId", null));
    return obj;
  }//</editor-fold>

  //<editor-fold defaultstate="collapsed" desc="get by id">
  @GET
  @Path("/{id}")
  @ApiOperation(value = "Read customer")
  @ApiResponse(code = 200, message = "Success", response = Customer.class)
  public Response getById(@PathParam("id") Long id) {
    logger.info(String.format("getById : %s", id));
    Customer entity = this.customerRepo.findOne(id);
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
    int count = 0;
    int countError = 0;
    JsonArray array = new Gson().fromJson(formData, JsonArray.class);
    if (array != null && array.size() > 0) {
      long id = 0L;
      for (JsonElement ele : array) {
        try {
          id = ele.getAsLong();
          if (customerService.delete(id)) {
            count++;
          } else {
            countError++;
          }
        } catch (Exception ex) {
          logger.error(String.format("ERROR delete {%d}", id), ex);
          countError++;
        }
      }
    }
    json.addProperty("success", count);
    json.addProperty("error", countError);
    return Response.ok().entity(json.toString()).build();
  }//</editor-fold>
}
