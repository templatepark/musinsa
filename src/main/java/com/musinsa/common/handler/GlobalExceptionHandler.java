package com.musinsa.common.handler;

import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import com.musinsa.brand.application.exception.AlreadyBrandNameException;
import com.musinsa.product.application.exception.NotExistBrandException;
import com.musinsa.product.application.exception.NotExistCategoryException;
import com.musinsa.product.application.exception.NotExistProductException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(AlreadyBrandNameException.class)
    public ErrorResponse handleNotFoundException(AlreadyBrandNameException e) {
        logger.error(e.getMessage(), e);
        return ErrorResponse.create(e, HttpStatus.CONFLICT, e.getMessage());
    }

    @ExceptionHandler({
        NotExistBrandException.class,
        NotExistCategoryException.class,
        NotExistProductException.class
    })
    public ErrorResponse handleNotExistException(RuntimeException e) {
        logger.error(e.getMessage(), e);
        return ErrorResponse.create(e, HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ErrorResponse handleArgumentNotValidationException(MethodArgumentNotValidException e) {
        String errorMessages =
                e.getBindingResult().getFieldErrors().stream()
                        .map(
                                error ->
                                        String.join(
                                                ": ", error.getField(), error.getDefaultMessage()))
                        .collect(Collectors.joining("; "));
        logger.error(errorMessages, e);
        return ErrorResponse.create(e, e.getStatusCode(), errorMessages);
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    public ErrorResponse handleMethodValidationException(HandlerMethodValidationException e) {
        logger.error(e.getMessage(), e);
        return ErrorResponse.create(e, e.getStatusCode(), e.getMessage());
    }
}
