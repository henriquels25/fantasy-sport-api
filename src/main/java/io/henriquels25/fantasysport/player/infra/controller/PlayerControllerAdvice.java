package io.henriquels25.fantasysport.player.infra.controller;

import io.henriquels25.fantasysport.infra.controller.exception.dto.Error;
import io.henriquels25.fantasysport.player.exception.TeamNotExistsException;
import io.henriquels25.fantasysport.player.exception.TeamServiceUnavailableException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;

@RestControllerAdvice
class PlayerControllerAdvice {
    @ExceptionHandler(TeamNotExistsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Mono<Error> teamNotExistsExceptionHandler(TeamNotExistsException ex) {
        return Mono.just(Error.builder().code("invalid_team")
                .description(ex.getMessage()).build());
    }

    @ExceptionHandler(TeamServiceUnavailableException.class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public Mono<Error> teamServiceUnavailableExceptionHandler(TeamServiceUnavailableException ex) {
        return Mono.just(Error.builder().code("team_api_unavailable")
                .description("Teams API is not available").build());
    }
}
