package com.backend_ecommerce.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

public class ApiResponse {

    //Created
    public static <T> ResponseEntity<ResponseData<T>> created(String message, T data) {
        ResponseData<T> responseData = new ResponseData<>();
        responseData.setStatus(HttpStatus.CREATED.value());
        responseData.setMessage(message);
        responseData.setData(data);
        return new ResponseEntity<>(responseData, HttpStatus.CREATED);
    }

    public static <T> ResponseEntity<ResponseData<T>> created(String message) {
        ResponseData<T> responseData = new ResponseData<>();
        responseData.setStatus(HttpStatus.CREATED.value());
        responseData.setMessage(message);
        responseData.setData(null);
        return new ResponseEntity<>(responseData, HttpStatus.CREATED);
    }

    //Oke
    public static <T> ResponseEntity<ResponseData<T>> ok(String message, T data) {
        ResponseData<T> responseData = new ResponseData<>();
        responseData.setStatus(HttpStatus.OK.value());
        responseData.setMessage(message);
        responseData.setData(data);
        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }

    public static <T> ResponseEntity<ResponseData<T>> ok(String message) {
        ResponseData<T> responseData = new ResponseData<>();
        responseData.setStatus(HttpStatus.OK.value());
        responseData.setMessage(message);
        responseData.setData(null);
        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }

    //Accept
    public static <T> ResponseEntity<ResponseData<T>> accepted(String message, T data) {
        ResponseData<T> responseData = new ResponseData<>();
        responseData.setStatus(HttpStatus.ACCEPTED.value());
        responseData.setMessage(message);
        responseData.setData(data);
        return new ResponseEntity<>(responseData, HttpStatus.ACCEPTED);
    }

    public static <T> ResponseEntity<ResponseData<T>> accepted(String message) {
        ResponseData<T> responseData = new ResponseData<>();
        responseData.setStatus(HttpStatus.ACCEPTED.value());
        responseData.setMessage(message);
        responseData.setData(null);
        return new ResponseEntity<>(responseData, HttpStatus.ACCEPTED);
    }

    public static <T> ResponseEntity<ResponseData<T>> noContent(String message) {
        ResponseData<T> responseData = new ResponseData<>();
        responseData.setStatus(HttpStatus.NO_CONTENT.value());
        responseData.setMessage(message);
        responseData.setData(null);
        return new ResponseEntity<>(responseData, HttpStatus.NO_CONTENT);
    }

    //Errors

    //Unauthorized
    public static <T> ResponseEntity<ErrorDetailsResponse> notFound(String message, T data) {
        ErrorDetailsResponse errorDetails = new ErrorDetailsResponse();
        errorDetails.setError(message);
        errorDetails.setDetails(data);
        errorDetails.setTimestamp(LocalDateTime.now());
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    //Unauthorized
    public static <T> ResponseEntity<ErrorDetailsResponse> unauthorized(String message, T data) {
        ErrorDetailsResponse errorDetails = new ErrorDetailsResponse();
        errorDetails.setError(message);
        errorDetails.setDetails(data);
        errorDetails.setTimestamp(LocalDateTime.now());
        return new ResponseEntity<>(errorDetails, HttpStatus.UNAUTHORIZED);
    }

    //Forbidden
    public static <T> ResponseEntity<ErrorDetailsResponse> forbidden(String message, T data) {
        ErrorDetailsResponse errorDetails = new ErrorDetailsResponse();
        errorDetails.setError(message);
        errorDetails.setDetails(data);
        errorDetails.setTimestamp(LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorDetails);
    }



    public static <T> ResponseEntity<ErrorDetailsResponse> badRequest(String message, T data) {
        ErrorDetailsResponse errorDetails = new ErrorDetailsResponse();
        errorDetails.setError(message);
        errorDetails.setDetails(data);
        errorDetails.setTimestamp(LocalDateTime.now());
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }
    public static <T> ResponseEntity<ErrorDetailsResponse> badRequest(String message) {
        ErrorDetailsResponse errorDetails = new ErrorDetailsResponse();
        errorDetails.setError(message);
        errorDetails.setDetails(null);
        errorDetails.setTimestamp(LocalDateTime.now());
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }
}
