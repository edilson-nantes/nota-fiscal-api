package com.edilson.exception.notaFiscal;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class NotaFiscalNotFoundExceptionMapper implements ExceptionMapper<NotaFiscalNotFoundException> {

    @Override
    public Response toResponse(NotaFiscalNotFoundException exception) {
        return Response.status(Response.Status.NOT_FOUND.getStatusCode(), "Nota Fiscal not found")
            .build();
    }
    
}
