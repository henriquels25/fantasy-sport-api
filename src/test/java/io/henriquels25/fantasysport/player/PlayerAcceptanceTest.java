package io.henriquels25.fantasysport.player;

import io.henriquels25.fantasysport.annotations.AcceptanceTest;
import io.henriquels25.fantasysport.player.infra.mongo.MongoTestHelper;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import static io.henriquels25.fantasysport.player.AcceptanceTestKafkaTestData.ACCEPTANCE_TEST_CREATED_EVENT;
import static io.henriquels25.fantasysport.player.AcceptanceTestKafkaTestData.ACCEPTANCE_TEST_UPDATED_EVENT;
import static io.henriquels25.fantasysport.player.factories.PlayerFactory.*;
import static io.henriquels25.fantasysport.player.infra.kafka.KafkaTestHelper.createConsumer;
import static io.henriquels25.fantasysport.player.infra.kafka.KafkaTestHelper.getNextRecord;
import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureDataMongo
@AutoConfigureWebTestClient
@AutoConfigureWireMock(port = 0)
@EmbeddedKafka(partitions = 1,
        topics = {"player-events-v1"},
        bootstrapServersProperty = "spring.kafka.bootstrap-servers")
@Import(MongoTestHelper.class)
class PlayerAcceptanceTest {

    @Autowired
    private WebTestClient webClient;

    @Autowired
    private MongoTestHelper mongoTestHelper;

    @Autowired
    private EmbeddedKafkaBroker embeddedKafka;

    @DisplayName("As a user, I want to list the players and add a new one")
    @AcceptanceTest
    void listPlayers() {
        Consumer<String, String> consumer = createConsumer(embeddedKafka,
                "player-events-v1");
        
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
                .jsonPath("[0].teamId").isEqualTo("idGremio")
                .jsonPath("[1].id").isEqualTo(idFernando)
                .jsonPath("[1].name").isEqualTo("Fernando")
                .jsonPath("[1].position").isEqualTo("CF")
                .jsonPath("[1].teamId", equalTo("idBarcelona"));

        webClient.post().uri("/players")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(diegoWithId(null))
                .exchange().expectStatus().isCreated();

        webClient.get().uri("/players/")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$").value(hasSize(3));

        ConsumerRecord<String, String> message = getNextRecord(consumer);
        assertThatJson(message.value()).isEqualTo(ACCEPTANCE_TEST_CREATED_EVENT);
    }

    @DisplayName("As a user, I want to update a player")
    @AcceptanceTest
    void updatePlayer() {
        Consumer<String, String> consumer = createConsumer(embeddedKafka,
                "player-events-v1");

        String id = mongoTestHelper.save(henrique()).block();
        webClient.get().uri("/players/")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$").value(hasSize(1))
                .jsonPath("[0].name").isEqualTo("Henrique")
                .jsonPath("[0].position").isEqualTo("GK")
                .jsonPath("[0].teamId").isEqualTo("idGremio");

        Player player = new Player(null, "updatedName", "CF", "idInternacional");

        webClient.put().uri("/players/{id}", id).bodyValue(player)
                .exchange().expectStatus().isNoContent();

        webClient.get().uri("/players/")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$").value(hasSize(1))
                .jsonPath("[0].name").isEqualTo("updatedName")
                .jsonPath("[0].position").isEqualTo("CF")
                .jsonPath("[0].teamId").isEqualTo("idInternacional");

        ConsumerRecord<String, String> message = getNextRecord(consumer);
        assertThatJson(message.value()).isEqualTo(ACCEPTANCE_TEST_UPDATED_EVENT);
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
                .jsonPath("$.teamId").isEqualTo("idGremio");
    }

    @DisplayName("As a user, I should not be able to create a player with an invalid team")
    @AcceptanceTest
    void createPlayerWithInvalidTeam() {
        Player player = new Player(null, "test", "CF",
                "idInvalid");

        webClient.post().uri("/players")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(player)
                .exchange().expectStatus().isBadRequest();
    }


    @AfterEach
    void cleanUp() {
        mongoTestHelper.dropPlayerCollection().block();
    }
}
