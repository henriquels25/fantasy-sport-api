package io.henriquels25.fantasysport.player.infra.controller;

import io.henriquels25.fantasysport.annotations.IntegrationTest;
import io.henriquels25.fantasysport.infra.ErrorTestDTO;
import io.henriquels25.fantasysport.player.Player;
import io.henriquels25.fantasysport.player.PlayerOperations;
import io.henriquels25.fantasysport.player.exception.TeamNotExistsException;
import io.henriquels25.fantasysport.player.exception.TeamServiceUnavailableException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
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
    private PlayerOperations playerOperations;

    @IntegrationTest
    void shouldReturnTheListOfPlayers() {
        when(playerOperations.allPlayers()).thenReturn(Flux.just(henrique(), fernando()));

        webTestClient.get().uri("/players")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("[0].id").isEqualTo("idHenrique")
                .jsonPath("[0].name").isEqualTo("Henrique")
                .jsonPath("[0].position").isEqualTo("GK")
                .jsonPath("[0].teamId").isEqualTo("idGremio")
                .jsonPath("[1].id").isEqualTo("idFernando")
                .jsonPath("[1].name").isEqualTo("Fernando")
                .jsonPath("[1].position").isEqualTo("CF")
                .jsonPath("[1].teamId", equalTo("idBarcelona"));
    }

    @IntegrationTest
    void shouldCreateAPlayer() {
        when(playerOperations.create(diegoWithId(null))).thenReturn(Mono.just("id1"));

        webTestClient.post().uri("/players")
                .bodyValue(diegoWithId(null))
                .exchange()
                .expectStatus().isCreated()
                .expectHeader()
                .valueEquals("location", "/players/id1");
    }

    @IntegrationTest
    void shouldUpdateAPlayer() {
        when(playerOperations.update("id1", diegoWithId(null))).thenReturn(Mono.empty());

        webTestClient.put().uri("/players/{id}", "id1")
                .bodyValue(diegoWithId(null))
                .exchange()
                .expectStatus().isNoContent();

        verify(playerOperations).update("id1", diegoWithId(null));
    }

    @IntegrationTest
    void shouldDeleteAPlayer() {
        when(playerOperations.delete("id1")).thenReturn(Mono.empty());

        webTestClient.delete().uri("/players/{id}", "id1")
                .exchange()
                .expectStatus().isNoContent();

        verify(playerOperations).delete("id1");
    }

    @IntegrationTest
    void shouldFindAPlayer() {
        when(playerOperations.findById("idHenrique")).thenReturn(Mono.just(henrique()));

        webTestClient.get().uri("/players/{id}", "idHenrique")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo("idHenrique")
                .jsonPath("$.name").isEqualTo("Henrique")
                .jsonPath("$.position").isEqualTo("GK")
                .jsonPath("$.teamId").isEqualTo("idGremio");

        verify(playerOperations).findById("idHenrique");
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

        verifyNoInteractions(playerOperations);
    }

    @IntegrationTest
    void shouldNotCreateAPlayerWithoutPositionAndTeam() {
        Player invalidPlayer = new Player(null, "Henrique", null, null);
        ErrorTestDTO.ErrorTestDetailDTO positionError =
                new ErrorTestDTO.ErrorTestDetailDTO("position", "must not be empty");
        ErrorTestDTO.ErrorTestDetailDTO teamError =
                new ErrorTestDTO.ErrorTestDetailDTO("teamId", "must not be empty");

        webTestClient
                .post().uri("/players")
                .bodyValue(invalidPlayer)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(ErrorTestDTO.class)
                .value(ErrorTestDTO::getCode, equalTo("field_validation_error"))
                .value(ErrorTestDTO::getDetails, containsInAnyOrder(teamError, positionError));

        verifyNoInteractions(playerOperations);
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

        verifyNoInteractions(playerOperations);
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

        verifyNoInteractions(playerOperations);
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

        verifyNoInteractions(playerOperations);
    }

    @IntegrationTest
    void shouldReturn503WhenTeamServiceIsUnavailable() {
        when(playerOperations.create(diegoWithId(null))).thenReturn(Mono.error(TeamServiceUnavailableException::new));

        webTestClient.post().uri("/players")
                .bodyValue(diegoWithId(null))
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.SERVICE_UNAVAILABLE)
                .expectBody()
                .jsonPath("$.code").isEqualTo("team_api_unavailable")
                .jsonPath("$.description").isEqualTo("Teams API is not available");
    }

    @IntegrationTest
    void shouldReturn400WhenTeamDoesNotExist() {
        when(playerOperations.create(diegoWithId(null))).thenReturn(Mono.error(() ->
                new TeamNotExistsException("Team test does not exist")));

        webTestClient.post().uri("/players")
                .bodyValue(diegoWithId(null))
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.BAD_REQUEST)
                .expectBody()
                .jsonPath("$.code").isEqualTo("invalid_team")
                .jsonPath("$.description").isEqualTo("Team test does not exist");
    }
}
