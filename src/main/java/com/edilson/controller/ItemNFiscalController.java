package com.edilson.controller;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

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
@Tag(name = "Item Nota Fiscal", description = "Endpoints relacionados a Itens de Notas Fiscais")
public class ItemNFiscalController {

    @Inject
    ItemNfiscalService itemNfiscalService;
    
    @GET
    @Operation(summary = "Listar todos os itens de notas fiscais", description = "Retorna uma lista paginada de todos os itens de notas fiscais")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Lista de itens de notas fiscais", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ItemNfiscalEntity.class)))
    })
    public Response findAll(@QueryParam("page") @DefaultValue("0") Integer page,
                            @QueryParam("pageSize") @DefaultValue("10") Integer pageSize) {
        return Response.ok(itemNfiscalService.findAll(page, pageSize)).build();
    }

    @POST
    @Transactional
    @Operation(summary = "Criar um novo item de nota fiscal", description = "Cria um novo item de nota fiscal")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Item de nota fiscal criado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ItemNfiscalEntity.class)))
    })
    public Response createItemNFiscal(ItemNfiscalEntity itemNfiscalEntity) {
        return Response.ok(itemNfiscalService.createItemNfiscal(itemNfiscalEntity)).build();
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "Buscar item de nota fiscal por ID", description = "Retorna um item de nota fiscal pelo ID")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Item de nota fiscal encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ItemNfiscalEntity.class))),
        @APIResponse(responseCode = "404", description = "Item de nota fiscal não encontrado")
    })
    public Response findById(@PathParam("id") Long id) {
        return Response.ok(itemNfiscalService.findById(id)).build();
    }

    @PUT
    @Transactional
    @Path("/{id}")
    @Operation(summary = "Atualizar um item de nota fiscal", description = "Atualiza um item de nota fiscal existente")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Item de nota fiscal atualizado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ItemNfiscalEntity.class))),
        @APIResponse(responseCode = "404", description = "Item de nota fiscal não encontrado")
    })
    public Response updateItemNFiscal(@PathParam("id") Long id, ItemNfiscalEntity itemNfiscalEntity) {
        return Response.ok(itemNfiscalService.updateItemNfiscal(id, itemNfiscalEntity)).build();
    }

    @DELETE
    @Transactional
    @Path("/{id}")
    @Operation(summary = "Excluir um item de nota fiscal", description = "Exclui um item de nota fiscal existente")
    @APIResponses(value = {
        @APIResponse(responseCode = "204", description = "Item de nota fiscal excluído"),
        @APIResponse(responseCode = "404", description = "Item de nota fiscal não encontrado")
    })
    public Response deleteItemNFiscal(@PathParam("id") Long id) {
        itemNfiscalService.deleteItemNfiscal(id);
        return Response.noContent().build();
    }
}
