package io.henriquels25.fantasysport.player.infra.mongo;

import io.henriquels25.fantasysport.player.Player;
import io.henriquels25.fantasysport.player.PlayerRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@AllArgsConstructor
class MongoPlayerRepository implements PlayerRepository {

    private final PlayerReactiveRepository repository;

    @Override
    public Flux<Player> findAll() {
        return repository.findAll().map(this::toPlayer);
    }

    @Override
    public Mono<String> save(Player player) {
        return repository.save(toDocument(player))
                .map(PlayerDocument::getId);
    }

    private PlayerDocument toDocument(Player player) {
        PlayerDocument playerDocument = new PlayerDocument();
        playerDocument.setTeam(player.getTeam());
        playerDocument.setPosition(player.getPosition());
        playerDocument.setName(player.getName());
        return playerDocument;
    }

    private Player toPlayer(PlayerDocument document) {
        return new Player(document.getName(), document.getPosition(), document.getTeam());
    }
}
