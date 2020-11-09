package io.henriquels25.fantasysport.player;

import io.henriquels25.fantasysport.annotations.AcceptanceTest;
import io.henriquels25.fantasysport.player.infra.mongo.MongoTestHelper;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.reactive.server.WebTestClient;

import static io.henriquels25.fantasysport.player.factories.PlayerFactory.fernando;
import static io.henriquels25.fantasysport.player.factories.PlayerFactory.henrique;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest
@AutoConfigureDataMongo
@AutoConfigureWebTestClient
@Import(MongoTestHelper.class)
class PlayerAcceptanceTest {

    @Autowired
    private WebTestClient webClient;

    @Autowired
    private MongoTestHelper mongoTestHelper;

    @DisplayName("As a user, I want to list the players")
    @AcceptanceTest
    void listPlayers() {
        mongoTestHelper.save(henrique()).block();
        mongoTestHelper.save(fernando()).block();

        webClient.get().uri("/players/")
                .exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("[0].name").isEqualTo("Henrique")
                .jsonPath("[0].position").isEqualTo("GK")
                .jsonPath("[0].team").isEqualTo("Gremio")
                .jsonPath("[1].name").isEqualTo("Fernando")
                .jsonPath("[1].position").isEqualTo("CF")
                .jsonPath("[1].team", equalTo("Barcelona"));
    }

}