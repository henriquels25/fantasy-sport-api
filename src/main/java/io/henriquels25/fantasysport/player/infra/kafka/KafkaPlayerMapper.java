package io.henriquels25.fantasysport.player.infra.kafka;

import io.henriquels25.fantasysport.player.Player;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
interface KafkaPlayerMapper {
    
    PlayerDTO toPlayerDTO(Player player);

}
