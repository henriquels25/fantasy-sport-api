package io.henriquels25.fantasysport.player;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PlayerRepository {
    Flux<Player> findAll();

    Mono<String> save(Player player);

    Mono<Void> update(String id, Player player);

    Mono<Void> delete(String id);
}
