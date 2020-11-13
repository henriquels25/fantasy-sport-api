package io.henriquels25.fantasysport.player.infra.controller;

import io.henriquels25.fantasysport.annotations.IntegrationTest;
import io.henriquels25.fantasysport.player.PlayerFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static io.henriquels25.fantasysport.player.factories.PlayerFactory.*;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@WebFluxTest(PlayerController.class)
class PlayerControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private PlayerFacade playerFacade;

    @IntegrationTest
    void shouldReturnTheListOfPlayers() {
        when(playerFacade.allPlayers()).thenReturn(Flux.just(henrique(), fernando()));

        webTestClient.get().uri("/players")
                .exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("[0].name").isEqualTo("Henrique")
                .jsonPath("[0].position").isEqualTo("GK")
                .jsonPath("[0].team").isEqualTo("Gremio")
                .jsonPath("[1].name").isEqualTo("Fernando")
                .jsonPath("[1].position").isEqualTo("CF")
                .jsonPath("[1].team", equalTo("Barcelona"));
    }

    @IntegrationTest
    void shouldSaveAPlayer() {
        when(playerFacade.create(diego())).thenReturn(Mono.just("id1"));

        webTestClient.post().uri("/players")
                .bodyValue(diego())
                .exchange()
                .expectStatus().isCreated()
                .expectHeader()
                .valueEquals("location", "/players/id1");
    }

    @IntegrationTest
    void shouldUpdateAPlayer() {
        when(playerFacade.update("id1", diego())).thenReturn(Mono.empty());

        webTestClient.put().uri("/players/{id}", "id1")
                .bodyValue(diego())
                .exchange()
                .expectStatus().isNoContent();

        verify(playerFacade).update("id1", diego());
    }

    @IntegrationTest
    void shouldDeleteAPlayer() {
        when(playerFacade.delete("id1")).thenReturn(Mono.empty());

        webTestClient.delete().uri("/players/{id}", "id1")
                .exchange()
                .expectStatus().isNoContent();

        verify(playerFacade).delete("id1");
    }

    @IntegrationTest
    void shouldFindAPlayer() {
        when(playerFacade.findById("id1")).thenReturn(Mono.just(henrique()));

        webTestClient.get().uri("/players/{id}", "id1")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.name").isEqualTo("Henrique")
                .jsonPath("$.position").isEqualTo("GK")
                .jsonPath("$.team").isEqualTo("Gremio");

        verify(playerFacade).findById("id1");
    }
}
