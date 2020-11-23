package io.henriquels25.fantasysport.player;

import io.henriquels25.fantasysport.player.exception.TeamNotExistsException;
import io.henriquels25.fantasysport.player.infra.client.TeamClient;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@AllArgsConstructor
@Service
public class PlayerFacade {

    private final PlayerRepository playerRepository;
    private final TeamClient teamClient;
    private final PlayerNotification playerNotification;

    public Flux<Player> allPlayers() {
        return playerRepository.findAll();
    }

    public Mono<String> create(Player player) {
        return teamClient.exists(player.getTeamId())
                .map(exists -> this.validateIfTeamExists(exists, player))
                .flatMap(b -> playerRepository.save(player))
                .flatMap(id -> playerNotification.notificateCreated(playerWithId(player, id)).thenReturn(id));
    }

    public Mono<Void> update(String id, Player player) {
        return teamClient.exists(player.getTeamId())
                .map(exists -> this.validateIfTeamExists(exists, player))
                .flatMap(p -> playerRepository.update(id, player))
                .then(Mono.defer(() -> playerNotification.notificateUpdated(playerWithId(player, id))));
    }

    public Mono<Void> delete(String id) {
        return playerRepository.delete(id);
    }

    public Mono<Player> findById(String id) {
        return playerRepository.findById(id);
    }

    private boolean validateIfTeamExists(boolean exists, Player player) {
        if (!exists) {
            throw new TeamNotExistsException
                    (String.format("team %s does not exist", player.getTeamId()));
        }
        return true;
    }

    private Player playerWithId(Player player, String id) {
        return player.toBuilder().id(id).build();
    }
}
