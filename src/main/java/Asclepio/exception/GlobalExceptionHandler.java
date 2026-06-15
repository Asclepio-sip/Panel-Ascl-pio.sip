package Asclepio.exception;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(

            ResourceNotFoundException ex,

            HttpServletRequest request) {

        ErrorResponse error = new ErrorResponse(

                LocalDateTime.now(),

                HttpStatus.NOT_FOUND.value(),

                "Not Found",

                ex.getMessage(),

                request.getRequestURI()

        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }


    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusiness(

            BusinessException ex,

            HttpServletRequest request) {

        ErrorResponse error = new ErrorResponse(

                LocalDateTime.now(),

                HttpStatus.BAD_REQUEST.value(),

                "Business Error",

                ex.getMessage(),

                request.getRequestURI()

        );

        return ResponseEntity.badRequest().body(error);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(

            Exception ex,

            HttpServletRequest request) {

        ErrorResponse error = new ErrorResponse(

                LocalDateTime.now(),

                HttpStatus.INTERNAL_SERVER_ERROR.value(),

                "Internal Server Error",

                ex.getMessage(),

                request.getRequestURI()

        );

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(error);
    }

}