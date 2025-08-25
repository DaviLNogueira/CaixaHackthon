package com.example.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ErrorResponse {
    private int status;
    private String response;
    private String detail;


    public ErrorResponse(String response, String detail, int status) {
        this.response = response;
        this.detail = detail;
        this.status = status;
    }

}
