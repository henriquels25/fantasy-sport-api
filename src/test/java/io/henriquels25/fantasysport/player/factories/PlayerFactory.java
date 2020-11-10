package io.henriquels25.fantasysport.player.factories;

import io.henriquels25.fantasysport.player.Player;

public class PlayerFactory {

    public static Player henrique() {
        return new Player("Henrique", "GK", "Gremio");
    }

    public static Player fernando() {
        return new Player("Fernando", "CF", "Barcelona");
    }

    public static Player diego() {
        return new Player("Diego", "CB", "Gremio");
    }

}
