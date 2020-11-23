package io.henriquels25.fantasysport.player;

import reactor.core.publisher.Mono;

public interface PlayerNotification {
    Mono<Void> notificateCreated(Player player);

    Mono<Void> notificateUpdated(Player player);
}
