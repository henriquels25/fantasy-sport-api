package io.henriquels25.fantasysport.player;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
@Builder(toBuilder = true, builderMethodName = "")
public final class Player {

    private final String id;
    private final String name;
    private final String position;
    private final String teamId;

}
