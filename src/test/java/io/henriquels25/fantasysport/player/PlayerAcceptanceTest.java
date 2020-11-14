package io.henriquels25.fantasysport.player;

import io.henriquels25.fantasysport.annotations.AcceptanceTest;
import io.henriquels25.fantasysport.player.infra.mongo.MongoTestHelper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import static io.henriquels25.fantasysport.player.factories.PlayerFactory.*;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

@SpringBootTest
@AutoConfigureDataMongo
@AutoConfigureWebTestClient
@Import(MongoTestHelper.class)
class PlayerAcceptanceTest {

    @Autowired
    private WebTestClient webClient;

    @Autowired
    private MongoTestHelper mongoTestHelper;

    @DisplayName("As a user, I want to list the players and add a new one")
    @AcceptanceTest
    void listPlayers() {
        String idHenrique = mongoTestHelper.save(henrique()).block();
        String idFernando = mongoTestHelper.save(fernando()).block();

        webClient.get().uri("/players/")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$").value(hasSize(2))
                .jsonPath("[0].id").isEqualTo(idHenrique)
                .jsonPath("[0].name").isEqualTo("Henrique")
                .jsonPath("[0].position").isEqualTo("GK")
                .jsonPath("[0].team").isEqualTo("Gremio")
                .jsonPath("[1].id").isEqualTo(idFernando)
                .jsonPath("[1].name").isEqualTo("Fernando")
                .jsonPath("[1].position").isEqualTo("CF")
                .jsonPath("[1].team", equalTo("Barcelona"));

        webClient.post().uri("/players")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(diego())
                .exchange().expectStatus().isCreated();

        webClient.get().uri("/players/")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$").value(hasSize(3));
    }

    @DisplayName("As a user, I want to update a player")
    @AcceptanceTest
    void updatePlayer() {
        String id = mongoTestHelper.save(henrique()).block();
        webClient.get().uri("/players/")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$").value(hasSize(1))
                .jsonPath("[0].name").isEqualTo("Henrique")
                .jsonPath("[0].position").isEqualTo("GK")
                .jsonPath("[0].team").isEqualTo("Gremio");

        Player player = new Player("updatedName", "CF", "Internacional");

        webClient.put().uri("/players/{id}", id).bodyValue(player)
                .exchange().expectStatus().isNoContent();

        webClient.get().uri("/players/")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$").value(hasSize(1))
                .jsonPath("[0].name").isEqualTo("updatedName")
                .jsonPath("[0].position").isEqualTo("CF")
                .jsonPath("[0].team").isEqualTo("Internacional");
    }

    @DisplayName("As a user, I want to delete a player")
    @AcceptanceTest
    void deletePlayer() {
        String idHenrique = mongoTestHelper.save(henrique()).block();
        mongoTestHelper.save(fernando()).block();
        webClient.get().uri("/players/")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$").value(hasSize(2));

        webClient.delete().uri("/players/{id}", idHenrique)
                .exchange().expectStatus().isNoContent();

        webClient.get().uri("/players/")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$").value(hasSize(1));
    }

    @DisplayName("As a user, I want to find a player by id")
    @AcceptanceTest
    void findPlayerById() {
        String idHenrique = mongoTestHelper.save(henrique()).block();

        webClient.get().uri("/players/{id}", idHenrique)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo(idHenrique)
                .jsonPath("$.name").isEqualTo("Henrique")
                .jsonPath("$.position").isEqualTo("GK")
                .jsonPath("$.team").isEqualTo("Gremio");
    }


    @AfterEach
    void cleanUp() {
        mongoTestHelper.dropPlayerCollection().block();
    }
}
