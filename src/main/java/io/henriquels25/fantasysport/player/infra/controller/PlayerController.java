package io.henriquels25.fantasysport.player.infra.controller;

import io.henriquels25.fantasysport.player.PlayerFacade;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/players")
@AllArgsConstructor
class PlayerController {

    private final PlayerFacade playerFacade;

    @GetMapping
    public Flux<PlayerDTO> allPlayers() {
        return playerFacade.allPlayers().map(PlayerDTO::fromPlayer);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Void> savePlayer(@RequestBody PlayerDTO player,
                                 ServerHttpResponse response,
                                 UriComponentsBuilder uriComponentsBuilder) {
        return playerFacade.create(player.toPlayer())
                .doOnNext(id -> setLocationHeader(response, uriComponentsBuilder, id))
                .then();
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> updatePlayer(@PathVariable String id, @RequestBody PlayerDTO player) {
        return playerFacade.update(id, player.toPlayer())
                .then();
    }

    private void setLocationHeader(ServerHttpResponse response,
                                   UriComponentsBuilder uriComponentsBuilder,
                                   String id) {
        response.getHeaders().setLocation(uriComponentsBuilder
                .path("/players/{id}")
                .buildAndExpand(id)
                .toUri());
    }
}
