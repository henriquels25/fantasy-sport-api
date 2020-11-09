package io.henriquels25.fantasysport.player.infra.controller;

import io.henriquels25.fantasysport.player.Player;
import io.henriquels25.fantasysport.player.PlayerFacade;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/players")
@AllArgsConstructor
class PlayerController {

    private final PlayerFacade playerFacade;

    @GetMapping
    Flux<Player> allPlayers() {
        return playerFacade.allPlayers();
    }

}
