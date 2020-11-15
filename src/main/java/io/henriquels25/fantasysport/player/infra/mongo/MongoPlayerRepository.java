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
    private final PlayerDocumentMapper mapper;

    @Override
    public Flux<Player> findAll() {
        return repository.findAll().map(mapper::toPlayer);
    }

    @Override
    public Mono<String> save(Player player) {
        return repository.save(mapper.toPlayerDocument(player))
                .map(PlayerDocument::getId);
    }

    @Override
    public Mono<Void> update(String id, Player player) {
        PlayerDocument playerDocument = mapper.toPlayerDocument(player);
        playerDocument.setId(id);
        return repository.save(playerDocument).then();
    }

    @Override
    public Mono<Void> delete(String id) {
        return repository.deleteById(id);
    }

    @Override
    public Mono<Player> findById(String id) {
        return repository.findById(id).map(mapper::toPlayer);
    }
}
