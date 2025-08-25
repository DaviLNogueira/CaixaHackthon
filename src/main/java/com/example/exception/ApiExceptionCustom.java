package com.example.exception;

import com.azure.core.annotation.Get;
import jakarta.ws.rs.core.Response;
import lombok.Getter;
import lombok.Setter;

@Getter
public class ApiExceptionCustom extends Exception {

    private String message;
    private Response.Status  status;

    public ApiExceptionCustom(String message, Response.Status status) {
        this.message = message;
        this.status = status;
    }
}
