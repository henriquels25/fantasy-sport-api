package io.henriquels25.fantasysport.player.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
public class TeamServiceUnavailableException extends RuntimeException {
}
