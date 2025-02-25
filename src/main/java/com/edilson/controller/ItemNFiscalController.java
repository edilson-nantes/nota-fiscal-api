package com.edilson.controller;

import com.edilson.entity.ItemNfiscalEntity;
import com.edilson.service.ItemNfiscalService;

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

@Path("/items-nfiscal")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ItemNFiscalController {

    @Inject
    ItemNfiscalService itemNfiscalService;
    
    @GET
    public Response findAll(@QueryParam("page") @DefaultValue("0") Integer page,
                            @QueryParam("pageSize") @DefaultValue("10") Integer pageSize) {
        return Response.ok(itemNfiscalService.findAll(page, pageSize)).build();
    }

    @POST
    @Transactional
    public Response createItemNFiscal(ItemNfiscalEntity itemNfiscalEntity) {
        return Response.ok(itemNfiscalService.createItemNfiscal(itemNfiscalEntity)).build();
    }

    @GET
    @Path("/{id}")
    public Response findById(@PathParam("id") Long id) {
        return Response.ok(itemNfiscalService.findById(id)).build();
    }

    @PUT
    @Transactional
    @Path("/{id}")
    public Response updateItemNFiscal(@PathParam("id") Long id, ItemNfiscalEntity itemNfiscalEntity) {
        return Response.ok(itemNfiscalService.updateItemNfiscal(id, itemNfiscalEntity)).build();
    }

    @DELETE
    @Transactional
    @Path("/{id}")
    public Response deleteItemNFiscal(@PathParam("id") Long id) {
        itemNfiscalService.deleteItemNfiscal(id);
        return Response.noContent().build();
    }
}
