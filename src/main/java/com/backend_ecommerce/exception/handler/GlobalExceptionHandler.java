package com.backend_ecommerce.exception.handler;

import com.backend_ecommerce.exception.*;
import com.backend_ecommerce.response.ApiResponse;
import com.backend_ecommerce.response.ErrorDetailsResponse;
import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    //Handler method argument not valid exception
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDetailsResponse> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException me){

        List<String> errors = new ArrayList<>();
        for(FieldError fieldError : me.getBindingResult().getFieldErrors()){
            errors.add(fieldError.getDefaultMessage());
        }

        return ApiResponse.badRequest("validation error", errors);
    }

    //Handler user not active exception
    @ExceptionHandler({DisabledException.class, InternalAuthenticationServiceException.class})
    public ResponseEntity<ErrorDetailsResponse> accountDisabledExceptionHandler(Exception de, WebRequest req){
        return ApiResponse.forbidden(de.getMessage(), req.getDescription(false));
    }

    //Handler methods
    //User exception
    //ProductException
    @ExceptionHandler({UserException.class,
            ProductException.class,
            VerificationCodeException.class,
            CartException.class,
            CategoryException.class,
            AttributeException.class,
            AddressException.class,
            BusinessException.class,
            OrderException.class,
            PaymentException.class,
            ReviewException.class,
    })
    public ResponseEntity<ErrorDetailsResponse> userExceptionHandler(Exception ue, WebRequest req){
        return ApiResponse.badRequest(ue.getMessage(), req.getDescription(false));
    }

    @ExceptionHandler({UsernameNotFoundException.class, ResourceNotFoundException.class})
    public ResponseEntity<ErrorDetailsResponse> usernameNotFoundExceptionHandler(Exception ue, WebRequest req){
        return ApiResponse.notFound(ue.getMessage(), req.getDescription(false));
    }

    //Validation exception handler
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorDetailsResponse> validationExceptionHandler(ValidationException ve){

        return ApiResponse.notFound(ve.getMessage(), ve.getErrors());
    }

    @ExceptionHandler(MessagingException.class)
    public ResponseEntity<ErrorDetailsResponse> messagingExceptionHandler(MessagingException me, WebRequest req){

        return ApiResponse.badRequest(me.getMessage(), req.getDescription(false));
    }

    //Jwt exception handler
    //Not use by filter chain
    @ExceptionHandler({JwtException.class})
    public ResponseEntity<ErrorDetailsResponse> jwtExceptionHandler(JwtException e, WebRequest req){

        return ApiResponse.unauthorized(e.getMessage(), req.getDescription(false));
    }

    //Incorrect json format
    @ExceptionHandler({HttpMessageNotReadableException.class})
    public ResponseEntity<ErrorDetailsResponse> httpMessageNoReadableExceptionHandler(HttpMessageNotReadableException e, WebRequest req){

        System.out.println(e.getMessage());

        ErrorDetailsResponse err = new ErrorDetailsResponse(
                "Json not format",
                req.getDescription(false),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(err, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    //Handler not found endpoint exception
    @ExceptionHandler({NoHandlerFoundException.class, NoResourceFoundException.class})
    public ResponseEntity<ErrorDetailsResponse> noHandlerFoundExceptionHandler(Exception ex, WebRequest req) {
        log.error(ex.getMessage(), ex);
        return ApiResponse.notFound("Endpoint not found", req.getDescription(false));
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<?> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException e, WebRequest req) {
        log.error(e.getMessage(), e);
        return ApiResponse.badRequest("Dung lượng tối đa 20MB", req.getDescription(false));
    }

    //Handler another exception
    @ExceptionHandler({Exception.class, RuntimeException.class})
    public ResponseEntity<ErrorDetailsResponse> otherExceptionHandler(Exception e, WebRequest req){
        return ApiResponse.badRequest(e.getMessage(), req.getDescription(false));
    }


    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorDetailsResponse> badCredentialsExceptionHandler(BadCredentialsException e, WebRequest req){
        return ApiResponse.unauthorized(e.getMessage(), req.getDescription(false));
    }
}
