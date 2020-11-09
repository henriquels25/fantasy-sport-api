package io.henriquels25.fantasysport.player.infra.mongo;

import io.henriquels25.fantasysport.player.Player;
import io.henriquels25.fantasysport.player.PlayerRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Component
@AllArgsConstructor
class MongoPlayerRepository implements PlayerRepository {

    private final PlayerReactiveRepository repository;

    @Override
    public Flux<Player> findAll() {
        return repository.findAll().map(this::toPlayer);
    }

    private Player toPlayer(PlayerDocument document) {
        return new Player(document.getName(), document.getPosition(), document.getTeam());
    }
}
