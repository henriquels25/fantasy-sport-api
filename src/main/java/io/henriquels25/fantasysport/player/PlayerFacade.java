package io.henriquels25.fantasysport.player;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@AllArgsConstructor
@Service
public class PlayerFacade {

    private final PlayerRepository playerRepository;

    public Flux<Player> allPlayers() {
        return playerRepository.findAll();
    }

}
