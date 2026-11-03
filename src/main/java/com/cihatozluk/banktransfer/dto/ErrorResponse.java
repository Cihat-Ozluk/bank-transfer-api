package com.cihatozluk.banktransfer.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.Instant;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorResponse(
        String errorCode,
        String message,
        Instant timestamp,
        Map<String,String> fieldErrors
) {
    public static ErrorResponse of(String errorCode,String message){
       return of(errorCode,message,null);
    }

    public static ErrorResponse of(String errorCode,String message,Map<String,String> fieldErrors){
        return new ErrorResponse(errorCode,message,Instant.now(),fieldErrors);
    }
}
