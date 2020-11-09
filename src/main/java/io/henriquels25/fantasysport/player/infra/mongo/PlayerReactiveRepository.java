package io.henriquels25.fantasysport.player.infra.mongo;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

interface PlayerReactiveRepository extends ReactiveMongoRepository<PlayerDocument, String> {
}
