package io.henriquels25.fantasysport.player.infra.kafka;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
class PlayerEventDTO {

    private String event;
    private PlayerDTO player;
}
