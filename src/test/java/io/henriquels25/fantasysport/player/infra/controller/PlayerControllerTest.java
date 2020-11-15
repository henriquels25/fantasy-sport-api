package io.henriquels25.fantasysport.player.infra.controller;

import io.henriquels25.fantasysport.annotations.IntegrationTest;
import io.henriquels25.fantasysport.infra.ErrorTestDTO;
import io.henriquels25.fantasysport.player.Player;
import io.henriquels25.fantasysport.player.PlayerFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static io.henriquels25.fantasysport.player.factories.PlayerFactory.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

@WebFluxTest(PlayerController.class)
@Import(PlayerMapperImpl.class)
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
                .expectBody()
                .jsonPath("[0].id").isEqualTo("idHenrique")
                .jsonPath("[0].name").isEqualTo("Henrique")
                .jsonPath("[0].position").isEqualTo("GK")
                .jsonPath("[0].team").isEqualTo("Gremio")
                .jsonPath("[1].id").isEqualTo("idFernando")
                .jsonPath("[1].name").isEqualTo("Fernando")
                .jsonPath("[1].position").isEqualTo("CF")
                .jsonPath("[1].team", equalTo("Barcelona"));
    }

    @IntegrationTest
    void shouldCreateAPlayer() {
        when(playerFacade.create(diegoWithId(null))).thenReturn(Mono.just("id1"));

        webTestClient.post().uri("/players")
                .bodyValue(diegoWithId(null))
                .exchange()
                .expectStatus().isCreated()
                .expectHeader()
                .valueEquals("location", "/players/id1");
    }

    @IntegrationTest
    void shouldUpdateAPlayer() {
        when(playerFacade.update("id1", diegoWithId(null))).thenReturn(Mono.empty());

        webTestClient.put().uri("/players/{id}", "id1")
                .bodyValue(diegoWithId(null))
                .exchange()
                .expectStatus().isNoContent();

        verify(playerFacade).update("id1", diegoWithId(null));
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
        when(playerFacade.findById("idHenrique")).thenReturn(Mono.just(henrique()));

        webTestClient.get().uri("/players/{id}", "idHenrique")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo("idHenrique")
                .jsonPath("$.name").isEqualTo("Henrique")
                .jsonPath("$.position").isEqualTo("GK")
                .jsonPath("$.team").isEqualTo("Gremio");

        verify(playerFacade).findById("idHenrique");
    }

    @IntegrationTest
    void shouldNotCreateAPlayerWithoutName() {
        Player playerWithoutName = new Player(null, null, "CF", "Gremio");

        webTestClient
                .post().uri("/players")
                .bodyValue(playerWithoutName)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.code").isEqualTo("field_validation_error")
                .jsonPath("$.description").doesNotExist()
                .jsonPath("$.details").value(hasSize(1))
                .jsonPath("$.details[0].name").isEqualTo("name")
                .jsonPath("$.details[0].description").isEqualTo("must not be empty");

        verifyNoInteractions(playerFacade);
    }

    @IntegrationTest
    void shouldNotCreateAPlayerWithoutPositionAndTeam() {
        Player invalidPlayer = new Player(null, "Henrique", null, null);
        ErrorTestDTO.ErrorTestDetailDTO positionError =
                new ErrorTestDTO.ErrorTestDetailDTO("position", "must not be empty");
        ErrorTestDTO.ErrorTestDetailDTO teamError =
                new ErrorTestDTO.ErrorTestDetailDTO("team", "must not be empty");

        webTestClient
                .post().uri("/players")
                .bodyValue(invalidPlayer)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(ErrorTestDTO.class)
                .value(ErrorTestDTO::getCode, equalTo("field_validation_error"))
                .value(ErrorTestDTO::getDetails, containsInAnyOrder(teamError, positionError));

        verifyNoInteractions(playerFacade);
    }

    @IntegrationTest
    void shouldNotUpdateAPlayerWithoutPositionAndName() {
        Player invalidPlayer = new Player(null, null, null, "Gremio");
        ErrorTestDTO.ErrorTestDetailDTO positionError =
                new ErrorTestDTO.ErrorTestDetailDTO("position", "must not be empty");
        ErrorTestDTO.ErrorTestDetailDTO nameError =
                new ErrorTestDTO.ErrorTestDetailDTO("name", "must not be empty");

        webTestClient
                .put().uri("/players/id1")
                .bodyValue(invalidPlayer)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(ErrorTestDTO.class)
                .value(ErrorTestDTO::getCode, equalTo("field_validation_error"))
                .value(ErrorTestDTO::getDetails, containsInAnyOrder(nameError, positionError));

        verifyNoInteractions(playerFacade);
    }


    @IntegrationTest
    void shouldNotCreateAPlayerWithId() {
        webTestClient
                .post().uri("/players")
                .bodyValue(diego())
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.code").isEqualTo("field_validation_error")
                .jsonPath("$.description").doesNotExist()
                .jsonPath("$.details").value(hasSize(1))
                .jsonPath("$.details[0].name").isEqualTo("id")
                .jsonPath("$.details[0].description").isEqualTo("must be null");

        verifyNoInteractions(playerFacade);
    }

    @IntegrationTest
    void shouldNotUpdateAPlayerWithId() {
        webTestClient
                .put().uri("/players/id1")
                .bodyValue(diego())
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.code").isEqualTo("field_validation_error")
                .jsonPath("$.description").doesNotExist()
                .jsonPath("$.details").value(hasSize(1))
                .jsonPath("$.details[0].name").isEqualTo("id")
                .jsonPath("$.details[0].description").isEqualTo("must be null");

        verifyNoInteractions(playerFacade);
    }
}
