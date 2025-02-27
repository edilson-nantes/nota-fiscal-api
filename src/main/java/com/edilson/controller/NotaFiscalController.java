package com.edilson.controller;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

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
@Tag(name = "Nota Fiscal", description = "Endpoints relacionados a Notas Fiscais")
public class NotaFiscalController {

    @Inject
    NotaFiscalService notaFiscalService;

    @GET
    @Operation(summary = "Listar todas as notas fiscais", description = "Retorna uma lista paginada de todas as notas fiscais")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Lista de notas fiscais", content = @Content(mediaType = "application/json", schema = @Schema(implementation = NotaFiscalEntity.class)))
    })
    public Response findAll(@QueryParam("page") @DefaultValue("0") Integer page,
                            @QueryParam("pageSize") @DefaultValue("10") Integer pageSize) {
        return Response.ok(notaFiscalService.findAll(page, pageSize)).build();
    }

    @POST
    @Transactional
    @Operation(summary = "Criar uma nova nota fiscal", description = "Cria uma nova nota fiscal")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Nota fiscal criada", content = @Content(mediaType = "application/json", schema = @Schema(implementation = NotaFiscalEntity.class)))
    })
    public Response createNotaFiscal(NotaFiscalEntity notaFiscalEntity) {
        return Response.ok(notaFiscalService.createNotaFiscal(notaFiscalEntity)).build();
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "Buscar nota fiscal por ID", description = "Retorna uma nota fiscal pelo ID")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Nota fiscal encontrada", content = @Content(mediaType = "application/json", schema = @Schema(implementation = NotaFiscalEntity.class))),
        @APIResponse(responseCode = "404", description = "Nota fiscal não encontrada")
    })
    public Response findById(@PathParam("id") Long id) {
        return Response.ok(notaFiscalService.findById(id)).build();
    }

    @PUT
    @Transactional
    @Path("/{id}")
    @Operation(summary = "Atualizar uma nota fiscal", description = "Atualiza uma nota fiscal existente")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Nota fiscal atualizada", content = @Content(mediaType = "application/json", schema = @Schema(implementation = NotaFiscalEntity.class))),
        @APIResponse(responseCode = "404", description = "Nota fiscal não encontrada")
    })
    public Response updateNotaFiscal(@PathParam("id") Long id, NotaFiscalEntity notaFiscalEntity) {
        return Response.ok(notaFiscalService.updateNotaFiscal(id, notaFiscalEntity)).build();
    }

    @DELETE
    @Transactional
    @Path("/{id}")
    @Operation(summary = "Excluir uma nota fiscal", description = "Exclui uma nota fiscal existente")
    @APIResponses(value = {
        @APIResponse(responseCode = "204", description = "Nota fiscal excluída"),
        @APIResponse(responseCode = "404", description = "Nota fiscal não encontrada")
    })
    public Response deleteNotaFiscal(@PathParam("id") Long id) {
        notaFiscalService.deleteNotaFiscal(id);
        return Response.noContent().build();
    }

    @GET
    @Path("/search")
    @Operation(summary = "Buscar notas fiscais por número", description = "Retorna uma lista de notas fiscais que correspondem ao número fornecido")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Lista de notas fiscais", content = @Content(mediaType = "application/json", schema = @Schema(implementation = NotaFiscalEntity.class)))
    })
    public Response searchNotas(@QueryParam("numberNota") String numberNota) {
        return Response.ok(notaFiscalService.searchNotas(numberNota)).build();
    }
    
}
