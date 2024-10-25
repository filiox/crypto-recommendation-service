package com.xm.crypto.recommendation.exception.handler;

import com.xm.crypto.recommendation.exception.CryptoDataMalformedException;
import com.xm.crypto.recommendation.exception.ResourceNotFoundException;
import com.xm.crypto.recommendation.exception.UnsupportedSymbolException;
import com.xm.crypto.recommendation.exception.ValidationException;
import com.xm.crypto.recommendation.model.ErrorDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CryptoDataMalformedException.class)
    public ResponseEntity<ErrorDetails> handleCryptoDataParsingException(CryptoDataMalformedException ex, WebRequest request) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(buildErrorResponse(HttpStatus.BAD_REQUEST, ex));
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorDetails> handleValidationException(ValidationException ex, WebRequest request) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(buildErrorResponse(HttpStatus.BAD_REQUEST, ex));
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorDetails> handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON)
                .body(buildErrorResponse(HttpStatus.NOT_FOUND, ex));
    }

    @ExceptionHandler(UnsupportedSymbolException.class)
    public ResponseEntity<ErrorDetails> handleUnsupportedSymbolException(UnsupportedSymbolException ex, WebRequest request) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(buildErrorResponse(HttpStatus.BAD_REQUEST, ex));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDetails> handleGenericException(Exception ex, WebRequest request) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(MediaType.APPLICATION_JSON)
                .body(buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex));
    }

    private ErrorDetails buildErrorResponse(HttpStatus status, Exception e) {
        return new ErrorDetails(status.getReasonPhrase(), e.getMessage());
    }
}