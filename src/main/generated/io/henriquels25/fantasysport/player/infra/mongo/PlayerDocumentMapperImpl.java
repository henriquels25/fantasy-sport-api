package io.henriquels25.fantasysport.player.infra.mongo;

import io.henriquels25.fantasysport.player.Player;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2020-11-14T22:04:04-0300",
    comments = "version: 1.4.1.Final, compiler: javac, environment: Java 15.0.1 (Oracle Corporation)"
)
@Component
class PlayerDocumentMapperImpl implements PlayerDocumentMapper {

    @Override
    public PlayerDocument toPlayerDocument(Player player) {
        if ( player == null ) {
            return null;
        }

        PlayerDocument playerDocument = new PlayerDocument();

        playerDocument.setId( player.getId() );
        playerDocument.setName( player.getName() );
        playerDocument.setPosition( player.getPosition() );
        playerDocument.setTeam( player.getTeam() );

        return playerDocument;
    }

    @Override
    public Player toPlayer(PlayerDocument playerDocument) {
        if ( playerDocument == null ) {
            return null;
        }

        String id = null;
        String name = null;
        String position = null;
        String team = null;

        id = playerDocument.getId();
        name = playerDocument.getName();
        position = playerDocument.getPosition();
        team = playerDocument.getTeam();

        Player player = new Player( id, name, position, team );

        return player;
    }
}
