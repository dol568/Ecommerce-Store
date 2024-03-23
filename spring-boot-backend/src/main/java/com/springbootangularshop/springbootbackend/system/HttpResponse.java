package com.springbootangularshop.springbootbackend.system;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.SuperBuilder;
import org.springframework.http.HttpStatus;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_DEFAULT;

@Data
@SuperBuilder
@JsonInclude(NON_DEFAULT)
public class HttpResponse<T> {
    private String timeStamp;
    private int statusCode;
    private HttpStatus status;
    private String message;
    private T data;

    public HttpResponse(String timeStamp, int statusCode, HttpStatus status, String message) {
        this.timeStamp = timeStamp;
        this.statusCode = statusCode;
        this.status = status;
        this.message = message;
    }
}
