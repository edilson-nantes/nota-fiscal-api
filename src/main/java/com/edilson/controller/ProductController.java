package com.edilson.controller;

import com.edilson.entity.ProductEntity;
import com.edilson.service.ProductService;

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

@Path("/products")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProductController {
    
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GET
    public Response findAll(@QueryParam("page") @DefaultValue("0") Integer page,
                            @QueryParam("pageSize") @DefaultValue("10") Integer pageSize) {
        
        var products = productService.findAll(page, pageSize);
        
        return Response.ok(products).build();
    }

    @POST
    @Transactional
    public Response createProduct(ProductEntity productEntity) {
        return Response.ok(productService.createProduct(productEntity)).build();
    }

    @GET
    @Path("/{id}")
    public Response findById(@PathParam("id") Long id) {
        return Response.ok(productService.findById(id)).build();
    }

    @PUT
    @Transactional
    @Path("/{id}")
    public Response updateProduct(@PathParam("id") Long id, ProductEntity productEntity) {
        
        return Response.ok(productService.updateProduct(id, productEntity)).build();
    }

    @DELETE
    @Transactional
    @Path("/{id}")
    public Response deleteProduct(@PathParam("id") Long id) {
        productService.deleteProduct(id);
        
        return Response.noContent().build();
    }
}
