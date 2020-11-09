package io.henriquels25.fantasysport.player.infra.mongo;

import io.henriquels25.fantasysport.annotations.IntegrationTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import reactor.test.StepVerifier;

import static io.henriquels25.fantasysport.player.factories.PlayerFactory.fernando;
import static io.henriquels25.fantasysport.player.factories.PlayerFactory.henrique;
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


}