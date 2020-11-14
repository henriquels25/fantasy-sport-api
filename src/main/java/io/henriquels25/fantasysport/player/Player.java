package io.henriquels25.fantasysport.player;


import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class Player {

    public Player(String name, String position, String team) {
        this.name = name;
        this.position = position;
        this.team = team;
        this.id = null;
    }

    private final String id;
    private final String name;
    private final String position;
    private final String team;

}
