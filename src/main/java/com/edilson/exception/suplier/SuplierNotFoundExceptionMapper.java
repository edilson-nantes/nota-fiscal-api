package com.edilson.exception.suplier;


import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class SuplierNotFoundExceptionMapper implements ExceptionMapper<SuplierNotFoundException> {

    @Override
    public Response toResponse(SuplierNotFoundException exception) {
        return Response.status(Response.Status.NOT_FOUND.getStatusCode(), "Suplier not found")
            .build();
    }
    
}
