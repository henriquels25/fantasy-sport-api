package io.henriquels25.fantasysport.infra.controller.exception;

import io.henriquels25.fantasysport.infra.controller.exception.dto.Error;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
class DefaultControllerAdvice {

    @ExceptionHandler(WebExchangeBindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Mono<Error> webExchangeBindExceptionHandler(WebExchangeBindException ex) {
        return Mono.just(Error.builder().code("field_validation_error").
                details(extractErrorDetails(ex)).build());
    }

    private List<Error.ErrorDetail> extractErrorDetails(WebExchangeBindException exception) {
        return exception.getFieldErrors().stream()
                .map(this::toErrorDetail).collect(Collectors.toList());
    }

    private Error.ErrorDetail toErrorDetail(FieldError fieldError) {
        return new Error.ErrorDetail(fieldError.getField(), fieldError.getDefaultMessage());
    }

}
