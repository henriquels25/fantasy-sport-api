package io.henriquels25.fantasysport.player.infra.mongo;

import io.henriquels25.fantasysport.annotations.IntegrationTest;
import io.henriquels25.fantasysport.player.Player;
import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import reactor.test.StepVerifier;

import static io.henriquels25.fantasysport.player.factories.PlayerFactory.*;
import static io.henriquels25.fantasysport.player.infra.mongo.MongoTestHelper.toDocument;

@DataMongoTest
@Import({MongoPlayerRepository.class, PlayerDocumentMapperImpl.class})
class MongoPlayerRepositoryTest {

    @Autowired
    private ReactiveMongoTemplate reactiveMongoTemplate;

    @Autowired
    private MongoPlayerRepository mongoPlayerRepository;

    @IntegrationTest
    void shouldReturnAllPlayers() {
        PlayerDocument savedHenrique = reactiveMongoTemplate.save(toDocument(henrique())).block();
        PlayerDocument savedFernando = reactiveMongoTemplate.save(toDocument(fernando())).block();

        Player expectedHenrique = toPlayer(savedHenrique);
        Player expectedFernando = toPlayer(savedFernando);

        StepVerifier.create(mongoPlayerRepository.findAll())
                .expectNext(expectedHenrique)
                .expectNext(expectedFernando)
                .expectComplete()
                .verify();
    }

    @IntegrationTest
    void shouldSaveAPlayer() {
        Player savedHenrique = toPlayer(reactiveMongoTemplate.save(toDocument(henrique())).block());

        String diegoId = mongoPlayerRepository.save(diego()).block();
        Player savedDiego = diegoWithId(diegoId);

        StepVerifier.create(mongoPlayerRepository.findAll())
                .expectNext(savedHenrique)
                .expectNext(savedDiego)
                .expectComplete()
                .verify();
    }

    @IntegrationTest
    void shouldUpdateAPlayer() {
        MongoTestHelper mongoTestHelper = new MongoTestHelper(reactiveMongoTemplate);
        String diegoId = mongoTestHelper.save(diego()).block();
        String henriqueId = mongoTestHelper.save(henrique()).block();
        Player savedDiego = diegoWithId(diegoId);
        Player savedHenrique = henriqueWithId(henriqueId);


        StepVerifier.create(mongoPlayerRepository.findAll())
                .expectNext(savedDiego)
                .expectNext(savedHenrique)
                .expectComplete()
                .verify();

        StepVerifier.create(mongoPlayerRepository.update(diegoId, fernando())).
                expectComplete().verify();

        StepVerifier.create(mongoPlayerRepository.findAll())
                .expectNext(fernandoWithId(diegoId))
                .expectNext(savedHenrique)
                .expectComplete()
                .verify();
    }

    @IntegrationTest
    void shouldDeleteAPlayer() {
        MongoTestHelper mongoTestHelper = new MongoTestHelper(reactiveMongoTemplate);
        String diegoId = mongoTestHelper.save(diego()).block();
        String henriqueId = mongoTestHelper.save(henrique()).block();
        Player savedDiego = diegoWithId(diegoId);
        Player savedHenrique = henriqueWithId(henriqueId);

        StepVerifier.create(mongoPlayerRepository.findAll())
                .expectNext(savedDiego)
                .expectNext(savedHenrique)
                .expectComplete()
                .verify();

        StepVerifier.create(mongoPlayerRepository.delete(diegoId)).
                expectComplete().verify();

        StepVerifier.create(mongoPlayerRepository.findAll())
                .expectNext(savedHenrique)
                .expectComplete()
                .verify();
    }

    @IntegrationTest
    void shouldFindAPlayerById() {
        MongoTestHelper mongoTestHelper = new MongoTestHelper(reactiveMongoTemplate);
        String id = mongoTestHelper.save(diego()).block();

        StepVerifier.create(mongoPlayerRepository.findById(id))
                .expectNext(diegoWithId(id))
                .verifyComplete();
    }

    @AfterEach
    void cleanUp() {
        reactiveMongoTemplate.dropCollection(PlayerDocument.class).block();
    }

    private Player toPlayer(PlayerDocument document) {
        return new Player(document.getId(), document.getName(),
                document.getPosition(), document.getTeamId());
    }

}