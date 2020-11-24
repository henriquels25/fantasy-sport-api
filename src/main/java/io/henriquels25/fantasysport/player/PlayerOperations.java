package io.henriquels25.fantasysport.player;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PlayerOperations {
    Flux<Player> allPlayers();

    Mono<String> create(Player player);

    Mono<Void> update(String id, Player player);

    Mono<Void> delete(String id);

    Mono<Player> findById(String id);
}
