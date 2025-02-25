package com.edilson.exception.suplier;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class InvalidSuplierAlterationExceptionMapper implements ExceptionMapper<InvalidSuplierAlterationException> {
    
    @Override
    public Response toResponse(InvalidSuplierAlterationException exception) {
        return Response.status(Response.Status.BAD_REQUEST).entity(exception.getMessage()).build();
    }
}
