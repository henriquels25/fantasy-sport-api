package io.henriquels25.fantasysport.player.infra.mongo;

import io.henriquels25.fantasysport.player.Player;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
interface PlayerDocumentMapper {

    PlayerDocument toPlayerDocument(Player player);

    Player toPlayer(PlayerDocument playerDocument);

}
