package io.henriquels25.fantasysport.player.infra.mongo;

import io.henriquels25.fantasysport.player.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;


@Component
public class MongoTestHelper {
    @Autowired
    private ReactiveMongoTemplate reactiveMongoTemplate;

    public Mono<Void> save(Player player) {
        return reactiveMongoTemplate.save(toDocument(player)).then();
    }

    public static PlayerDocument toDocument(Player player) {
        PlayerDocument playerDocument = new PlayerDocument();
        playerDocument.setName(player.getName());
        playerDocument.setPosition(player.getPosition());
        playerDocument.setTeam(player.getTeam());
        return playerDocument;
    }
}
