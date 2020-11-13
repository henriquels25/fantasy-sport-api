package io.henriquels25.fantasysport.player.infra.mongo;

import io.henriquels25.fantasysport.annotations.IntegrationTest;
import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import reactor.test.StepVerifier;

import static io.henriquels25.fantasysport.player.factories.PlayerFactory.*;
import static io.henriquels25.fantasysport.player.infra.mongo.MongoTestHelper.toDocument;

@DataMongoTest
@Import(MongoPlayerRepository.class)
class MongoPlayerRepositoryTest {

    @Autowired
    private ReactiveMongoTemplate reactiveMongoTemplate;

    @Autowired
    private MongoPlayerRepository mongoPlayerRepository;

    @IntegrationTest
    void shouldReturnAllPlayers() {
        reactiveMongoTemplate.save(toDocument(henrique())).block();
        reactiveMongoTemplate.save(toDocument(fernando())).block();

        StepVerifier.create(mongoPlayerRepository.findAll())
                .expectNext(henrique())
                .expectNext(fernando())
                .expectComplete()
                .verify();
    }

    @IntegrationTest
    void shouldSaveAPlayer() {
        reactiveMongoTemplate.save(toDocument(henrique())).block();

        StepVerifier.create(mongoPlayerRepository.save(diego())).
                expectNextCount(1).
                expectComplete().verify();

        StepVerifier.create(mongoPlayerRepository.findAll())
                .expectNext(henrique())
                .expectNext(diego())
                .expectComplete()
                .verify();
    }

    @IntegrationTest
    void shouldUpdateAPlayer() {
        MongoTestHelper mongoTestHelper = new MongoTestHelper(reactiveMongoTemplate);
        String id = mongoTestHelper.save(diego()).block();
        mongoTestHelper.save(henrique()).block();

        StepVerifier.create(mongoPlayerRepository.findAll())
                .expectNext(diego())
                .expectNext(henrique())
                .expectComplete()
                .verify();

        StepVerifier.create(mongoPlayerRepository.update(id, fernando())).
                expectComplete().verify();

        StepVerifier.create(mongoPlayerRepository.findAll())
                .expectNext(fernando())
                .expectNext(henrique())
                .expectComplete()
                .verify();
    }

    @AfterEach
    void cleanUp() {
        reactiveMongoTemplate.dropCollection(PlayerDocument.class).block();
    }

}