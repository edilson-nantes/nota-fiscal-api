package com.edilson.controller;

import com.edilson.entity.NotaFiscalEntity;
import com.edilson.service.NotaFiscalService;

import jakarta.transaction.Transactional;
import jakarta.inject.Inject;
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


@Path("/notas-fiscais")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class NotaFiscalController {

    @Inject
    NotaFiscalService notaFiscalService;

    @GET
    public Response findAll(@QueryParam("page") @DefaultValue("0") Integer page,
                            @QueryParam("pageSize") @DefaultValue("10") Integer pageSize) {
        return Response.ok(notaFiscalService.findAll(page, pageSize)).build();
    }

    @POST
    @Transactional
    public Response createNotaFiscal(NotaFiscalEntity notaFiscalEntity) {
        return Response.ok(notaFiscalService.createNotaFiscal(notaFiscalEntity)).build();
    }

    @GET
    @Path("/{id}")
    public Response findById(@PathParam("id") Long id) {
        return Response.ok(notaFiscalService.findById(id)).build();
    }

    @PUT
    @Transactional
    @Path("/{id}")
    public Response updateNotaFiscal(@PathParam("id") Long id, NotaFiscalEntity notaFiscalEntity) {
        return Response.ok(notaFiscalService.updateNotaFiscal(id, notaFiscalEntity)).build();
    }

    @DELETE
    @Transactional
    @Path("/{id}")
    public Response deleteNotaFiscal(@PathParam("id") Long id) {
        notaFiscalService.deleteNotaFiscal(id);
        return Response.noContent().build();
    }
    
}
