package com.edilson.controller;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import com.edilson.entity.SuplierEntity;
import com.edilson.service.SuplierService;

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

@Path("/supliers")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Suplier", description = "Endpoints relacionados a Fornecedores")
public class SuplierController {

    
    @Inject
    SuplierService suplierService;

    @GET
    @Operation(summary = "Listar todos os fornecedores", description = "Retorna uma lista paginada de todos os fornecedores")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Lista de fornecedores", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SuplierEntity.class)))
    })
    public Response findAll(@QueryParam("page") @DefaultValue("0") Integer page,
                            @QueryParam("pageSize") @DefaultValue("10") Integer pageSize) {
        return Response.ok(suplierService.findAll(page, pageSize)).build();
    }

    @POST
    @Transactional
    @Operation(summary = "Criar um novo fornecedor", description = "Cria um novo fornecedor")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Fornecedor criado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SuplierEntity.class)))
    })
    public Response createSuplier(SuplierEntity suplierEntity) {
        return Response.ok(suplierService.createSuplier(suplierEntity)).build();
    }
    
    @GET
    @Path("/{id}")
    @Operation(summary = "Buscar fornecedor por ID", description = "Retorna um fornecedor pelo ID")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Fornecedor encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SuplierEntity.class))),
        @APIResponse(responseCode = "404", description = "Fornecedor não encontrado")
    })
    public Response findById(@PathParam("id") Long id) {
        return Response.ok(suplierService.findById(id)).build();
    }

    @PUT
    @Transactional
    @Path("/{id}")
    @Operation(summary = "Atualizar um fornecedor", description = "Atualiza um fornecedor existente")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Fornecedor atualizado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SuplierEntity.class))),
        @APIResponse(responseCode = "404", description = "Fornecedor não encontrado")
    })
    public Response updateSuplier(@PathParam("id") Long id, SuplierEntity suplierEntity) {
        return Response.ok(suplierService.updateSuplier(id, suplierEntity)).build();
    }

    @DELETE
    @Transactional
    @Path("/{id}")
    @Operation(summary = "Excluir um fornecedor", description = "Exclui um fornecedor existente")
    @APIResponses(value = {
        @APIResponse(responseCode = "204", description = "Fornecedor excluído"),
        @APIResponse(responseCode = "404", description = "Fornecedor não encontrado")
    })
    public Response deleteSuplier(@PathParam("id") Long id) {
        suplierService.deleteSuplier(id);
        
        return Response.noContent().build();
    }

    @GET
    @Path("/search")
    @Operation(summary = "Buscar fornecedores por código, nome legal, email, telefone ou CNPJ", description = "Retorna uma lista de fornecedores que correspondem aos critérios fornecidos")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Lista de fornecedores", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SuplierEntity.class)))
    })
    public Response searchSupliers(@QueryParam("code") String code,
                                   @QueryParam("legalName") String legalName,
                                   @QueryParam("email") String email,
                                   @QueryParam("phone") String phone,
                                   @QueryParam("cnpj") String cnpj) {
        return Response.ok(suplierService.searchSupliers(code, legalName, email, phone, cnpj)).build();
    }
}
