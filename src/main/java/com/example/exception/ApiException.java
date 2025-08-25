package com.example.exception;

import jakarta.ws.rs.core.Response;
import lombok.Getter;

@Getter
public class ApiException extends Exception {

    private String message;
    private Response.Status  status = Response.Status.BAD_REQUEST;

    public ApiException(String message) {
        this.message = message;
    }
}
