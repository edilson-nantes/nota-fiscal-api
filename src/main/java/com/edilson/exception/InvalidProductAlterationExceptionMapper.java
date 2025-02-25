package com.edilson.exception;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class InvalidProductAlterationExceptionMapper implements ExceptionMapper<InvalidProductAlterationException> {
    
    @Override
    public Response toResponse(InvalidProductAlterationException exception) {
        return Response.status(Response.Status.BAD_REQUEST)
            .entity(exception.getMessage())
            .build();
    }
}
