package com.example.exception;

import com.example.model.dto.ErrorResponse;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class GenericExceptionMapper implements ExceptionMapper<Throwable> {

    @Override
    public Response toResponse(Throwable exception) {

        ErrorResponse error;
        if (exception instanceof ApiException apiExceptionCustom) {
             error = new ErrorResponse(
                    apiExceptionCustom.getStatus().toString(),
                     apiExceptionCustom.getMessage(),
                    apiExceptionCustom.getStatus().getStatusCode()
            );
        }
        else {
             error = new ErrorResponse(
                    "Ocorreu um erro ao processar a requisição",
                    exception.getMessage(),
                    Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()
            );
        }



        return Response.status(error.getStatus())
                .entity(error)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}
