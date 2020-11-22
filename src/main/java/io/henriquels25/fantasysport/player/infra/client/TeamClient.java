package io.henriquels25.fantasysport.player.infra.client;

import reactor.core.publisher.Mono;

public interface TeamClient {
    Mono<Boolean> exists(String teamId);
}
