package io.henriquels25.fantasysport.player.infra.controller;

import io.henriquels25.fantasysport.player.Player;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
interface PlayerMapper {

    Player toPlayer(PlayerDTO playerDTO);

    PlayerDTO toPlayerDTO(Player player);

}
