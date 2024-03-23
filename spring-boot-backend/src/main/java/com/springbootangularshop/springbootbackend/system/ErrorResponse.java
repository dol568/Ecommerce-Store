package com.springbootangularshop.springbootbackend.system;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.SuperBuilder;
import org.springframework.http.HttpStatus;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_DEFAULT;

@Data
@SuperBuilder
@JsonInclude(NON_DEFAULT)
public class ErrorResponse {
    private String timeStamp;
    private int statusCode;
    public HttpStatus status;
    protected String message;
}
