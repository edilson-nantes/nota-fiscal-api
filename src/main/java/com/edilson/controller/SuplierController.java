package com.edilson.controller;

import com.edilson.entity.SuplierEntity;
import com.edilson.service.SuplierService;

import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/supliers")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SuplierController {

    private final SuplierService suplierService;

    public SuplierController(SuplierService suplierService) {
        this.suplierService = suplierService;
    }

    @GET
    public Response findAll(@QueryParam("page") @DefaultValue("0") Integer page,
                            @QueryParam("pageSize") @DefaultValue("10") Integer pageSize) {
        return Response.ok(suplierService.findAll(page, pageSize)).build();
    }

    @POST
    @Transactional
    public Response createSuplier(SuplierEntity suplierEntity) {
        return Response.ok(suplierService.createSuplier(suplierEntity)).build();
    }
    
    @GET
    @Path("/{id}")
    public Response findById(@PathParam("id") Long id) {
        return Response.ok(suplierService.findById(id)).build();
    }

    @PUT
    @Transactional
    @Path("/{id}")
    public Response updateSuplier(@PathParam("id") Long id, SuplierEntity suplierEntity) {
        return Response.ok(suplierService.updateSuplier(id, suplierEntity)).build();
    }

    @DELETE
    @Transactional
    @Path("/{id}")
    public Response deleteSuplier(@PathParam("id") Long id) {
        suplierService.deleteSuplier(id);
        
        return Response.noContent().build();
    }
}
