package com.global.servicecommon.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.global.servicecommon.jdbc.SsdcEntity;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import java.io.Serializable;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by ThuyetLV
 */
public class SsdcCrudEndpoint<ID extends Serializable, T extends SsdcEntity<ID>> {

    private static Logger LOG = LoggerFactory.getLogger(SsdcCrudEndpoint.class);

    @Autowired
    protected HttpSession session;

    protected SsdcCrudService<ID, T> service;

    @GET
    public List<T> doFind() {
        return service.getAll();
    }

    @GET
    @Path("/{id}")
    public T doRead(@PathParam("id") ID id) {
        return service.get(id);
    }

    @POST
    public T doCreate(T entity) {
        return service.create(entity);
    }

    @PUT
    @Path("/{id}")
    public T doUpdate(@PathParam("id") ID id, T entity) {
        return service.update(id, entity);
    }

    @DELETE
    @Path("/{id}")
    public void doDelete(@PathParam("id") ID id) {
        service.delete(id);
    }
}
