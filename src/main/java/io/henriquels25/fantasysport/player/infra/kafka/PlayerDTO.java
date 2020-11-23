package io.henriquels25.fantasysport.player.infra.kafka;

import lombok.Data;

@Data
class PlayerDTO {
    private String id;
    private String name;
    private String position;
    private String teamId;

}
