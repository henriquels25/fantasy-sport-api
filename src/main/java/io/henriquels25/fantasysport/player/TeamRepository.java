package io.henriquels25.fantasysport.player;

import reactor.core.publisher.Mono;

public interface TeamRepository {
    Mono<Boolean> exists(String teamId);
}
