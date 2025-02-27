package com.edilson.controller;

import java.util.List;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import com.edilson.entity.ProductEntity;
import com.edilson.service.ProductService;

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

@Path("/products")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Product", description = "Endpoints relacionados a Produtos")
public class ProductController {
    
    @Inject
    ProductService productService;

    @GET
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Lista de produtos", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductEntity.class)))
    })
    public Response findAll(@QueryParam("page") @DefaultValue("0") Integer page,
                            @QueryParam("pageSize") @DefaultValue("10") Integer pageSize) {
        
        var products = productService.findAll(page, pageSize);
        
        return Response.ok(products).build();
    }

    @POST
    @Transactional
    @Operation(summary = "Criar um novo produto", description = "Cria um novo produto")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Produto criado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductEntity.class)))
    })
    public Response createProduct(ProductEntity productEntity) {
        return Response.ok(productService.createProduct(productEntity)).build();
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "Buscar produto por ID", description = "Retorna um produto pelo ID")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Produto encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductEntity.class))),
        @APIResponse(responseCode = "404", description = "Produto não encontrado")
    })
    public Response findById(@PathParam("id") Long id) {
        return Response.ok(productService.findById(id)).build();
    }

    @PUT
    @Transactional
    @Path("/{id}")
    @Operation(summary = "Atualizar um produto", description = "Atualiza um produto existente")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Produto atualizado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductEntity.class))),
        @APIResponse(responseCode = "404", description = "Produto não encontrado")
    })
    public Response updateProduct(@PathParam("id") Long id, ProductEntity productEntity) {
        
        return Response.ok(productService.updateProduct(id, productEntity)).build();
    }

    @DELETE
    @Transactional
    @Path("/{id}")
    @Operation(summary = "Excluir um produto", description = "Exclui um produto existente")
    @APIResponses(value = {
        @APIResponse(responseCode = "204", description = "Produto excluído"),
        @APIResponse(responseCode = "404", description = "Produto não encontrado")
    })
    public Response deleteProduct(@PathParam("id") Long id) {
        productService.deleteProduct(id);
        
        return Response.noContent().build();
    }

    @GET
    @Path("/search")
    @Operation(summary = "Buscar produtos por código ou descrição", description = "Retorna uma lista de produtos que correspondem ao código ou descrição fornecidos")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Lista de produtos", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductEntity.class)))
    })
    public Response searchProducts(@QueryParam("code") String code,
                                   @QueryParam("description") String description) {
        List<ProductEntity> products = productService.searchProducts(code, description);
        return Response.ok(products).type(MediaType.APPLICATION_JSON).build();
    }
}
