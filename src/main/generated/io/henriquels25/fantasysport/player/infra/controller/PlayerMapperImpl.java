package io.henriquels25.fantasysport.player.infra.controller;

import io.henriquels25.fantasysport.player.Player;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2020-11-14T22:02:55-0300",
    comments = "version: 1.4.1.Final, compiler: javac, environment: Java 15.0.1 (Oracle Corporation)"
)
@Component
class PlayerMapperImpl implements PlayerMapper {

    @Override
    public Player toPlayer(PlayerDTO playerDTO) {
        if ( playerDTO == null ) {
            return null;
        }

        String id = null;
        String name = null;
        String position = null;
        String team = null;

        id = playerDTO.getId();
        name = playerDTO.getName();
        position = playerDTO.getPosition();
        team = playerDTO.getTeam();

        Player player = new Player( id, name, position, team );

        return player;
    }

    @Override
    public PlayerDTO toPlayerDTO(Player player) {
        if ( player == null ) {
            return null;
        }

        PlayerDTO playerDTO = new PlayerDTO();

        playerDTO.setId( player.getId() );
        playerDTO.setName( player.getName() );
        playerDTO.setPosition( player.getPosition() );
        playerDTO.setTeam( player.getTeam() );

        return playerDTO;
    }
}
