package io.henriquels25.fantasysport.player.infra.controller;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Null;

@Data
class PlayerDTO {
    @Null
    private String id;

    @NotEmpty
    private String name;
    @NotEmpty
    private String position;
    @NotEmpty
    private String team;
}
