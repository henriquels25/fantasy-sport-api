package io.henriquels25.fantasysport.player.infra.mongo;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
class PlayerDocument {

    @Id
    private String id;

    private String name;
    private String position;
    private String team;
}
