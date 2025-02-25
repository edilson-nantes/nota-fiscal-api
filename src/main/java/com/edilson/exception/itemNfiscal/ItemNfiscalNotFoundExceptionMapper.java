package com.edilson.exception.itemNfiscal;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class ItemNfiscalNotFoundExceptionMapper implements ExceptionMapper<ItemNfiscalNotFoundException> {

    @Override
    public Response toResponse(ItemNfiscalNotFoundException exception) {
        return Response.status(Response.Status.NOT_FOUND).build();
    }
    
}
