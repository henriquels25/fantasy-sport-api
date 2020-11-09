package io.henriquels25.fantasysport.player;

import reactor.core.publisher.Flux;

public interface PlayerRepository {
    Flux<Player> findAll();
}
