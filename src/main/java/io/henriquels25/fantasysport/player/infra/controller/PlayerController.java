package io.henriquels25.fantasysport.player.infra.controller;

import io.henriquels25.fantasysport.player.PlayerFacade;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@RequestMapping("/players")
@AllArgsConstructor
class PlayerController {

    private final PlayerFacade playerFacade;
    private final PlayerMapper mapper;

    @GetMapping
    public Flux<PlayerDTO> allPlayers() {
        return playerFacade.allPlayers().map(mapper::toPlayerDTO);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Void> savePlayer(@RequestBody @Valid PlayerDTO player,
                                 ServerHttpResponse response,
                                 UriComponentsBuilder uriComponentsBuilder) {
        return playerFacade.create(mapper.toPlayer(player))
                .doOnNext(id -> setLocationHeader(response, uriComponentsBuilder, id))
                .then();
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> updatePlayer(@PathVariable String id, @RequestBody @Valid PlayerDTO player) {
        return playerFacade.update(id, mapper.toPlayer(player));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deletePlayer(@PathVariable String id) {
        return playerFacade.delete(id);
    }

    @GetMapping("/{id}")
    public Mono<PlayerDTO> findPlayerById(@PathVariable String id) {
        return playerFacade.findById(id).map(mapper::toPlayerDTO);
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
