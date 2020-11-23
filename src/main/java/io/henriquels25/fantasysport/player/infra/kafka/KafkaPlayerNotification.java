package io.henriquels25.fantasysport.player.infra.kafka;

import io.henriquels25.fantasysport.player.Player;
import io.henriquels25.fantasysport.player.PlayerNotification;
import lombok.AllArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@AllArgsConstructor
class KafkaPlayerNotification implements PlayerNotification {

    private final KafkaTemplate<String, PlayerEventDTO> kafkaTemplate;
    private final KafkaPlayerMapper kafkaPlayerMapper;

    @Override
    public Mono<Void> notificateCreated(Player player) {
        PlayerEventDTO playerEventDTO =
                new PlayerEventDTO("player_created",
                        kafkaPlayerMapper.toPlayerDTO(player));

        return Mono.fromFuture(kafkaTemplate
                .send("player-events-v1", playerEventDTO)
                .completable())
                .then();
    }

    @Override
    public Mono<Void> notificateUpdated(Player player) {
        PlayerEventDTO playerEventDTO =
                new PlayerEventDTO("player_updated",
                        kafkaPlayerMapper.toPlayerDTO(player));

        return Mono.fromFuture(kafkaTemplate
                .send("player-events-v1", playerEventDTO)
                .completable())
                .then();
    }
}
