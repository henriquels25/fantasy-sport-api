package io.henriquels25.fantasysport.player.infra.mongo;

import io.henriquels25.fantasysport.player.Player;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;


@Component
public class MongoTestHelper {

    private final ReactiveMongoTemplate reactiveMongoTemplate;

    public MongoTestHelper(ReactiveMongoTemplate reactiveMongoTemplate) {
        this.reactiveMongoTemplate = reactiveMongoTemplate;
    }

    public Mono<String> save(Player player) {
        return reactiveMongoTemplate.save(toDocument(player)).map(PlayerDocument::getId);
    }

    public Mono<Void> dropPlayerCollection() {
        return reactiveMongoTemplate.dropCollection(PlayerDocument.class).then();
    }

    public static PlayerDocument toDocument(Player player) {
        PlayerDocument playerDocument = new PlayerDocument();
        playerDocument.setName(player.getName());
        playerDocument.setPosition(player.getPosition());
        playerDocument.setTeam(player.getTeam());
        return playerDocument;
    }
}
