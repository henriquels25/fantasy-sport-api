package io.henriquels25.fantasysport.player;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PlayerRepository {
    Flux<Player> findAll();

    Mono<String> save(Player player);
}
