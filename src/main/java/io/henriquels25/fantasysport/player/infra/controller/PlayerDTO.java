package io.henriquels25.fantasysport.player.infra.controller;

import io.henriquels25.fantasysport.player.Player;
import lombok.Data;

@Data
class PlayerDTO {
    private String name;
    private String position;
    private String team;

    Player toPlayer() {
        return new Player(name, position, team);
    }

    static PlayerDTO fromPlayer(Player player) {
        PlayerDTO playerDTO = new PlayerDTO();
        playerDTO.setName(player.getName());
        playerDTO.setPosition(player.getPosition());
        playerDTO.setTeam(player.getTeam());
        return playerDTO;
    }
}
