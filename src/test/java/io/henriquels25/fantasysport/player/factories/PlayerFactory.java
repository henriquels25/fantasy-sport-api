package io.henriquels25.fantasysport.player.factories;

import io.henriquels25.fantasysport.player.Player;

public class PlayerFactory {

    public static Player henrique() {
        return new Player("idHenrique", "Henrique", "GK", "idGremio");
    }

    public static Player henriqueWithId(String id) {
        return new Player(id, "Henrique", "GK", "idGremio");
    }

    public static Player fernando() {
        return new Player("idFernando", "Fernando", "CF", "idBarcelona");
    }

    public static Player fernandoWithId(String id) {
        return new Player(id, "Fernando", "CF", "idBarcelona");
    }

    public static Player diego() {
        return new Player("idDiego", "Diego", "CB", "idGremio");
    }

    public static Player diegoWithId(String id) {
        return new Player(id, "Diego", "CB", "idGremio");
    }

}
