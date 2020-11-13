package io.henriquels25.fantasysport.player;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@AllArgsConstructor
@Service
public class PlayerFacade {

    private final PlayerRepository playerRepository;

    public Flux<Player> allPlayers() {
        return playerRepository.findAll();
    }

    public Mono<String> create(Player player) {
        return playerRepository.save(player);
    }

    public Mono<Void> update(String id, Player player) {
        return playerRepository.update(id, player);
    }
}
