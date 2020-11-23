package io.henriquels25.fantasysport.player.infra.kafka;

class PlayerKafkaTestData {

    static String DIEGO_CREATED_EVENT = "{\n" +
            "  \"event\": \"player_created\",\n" +
            "  \"player\": {\n" +
            "    \"id\": \"idDiego\",\n" +
            "    \"name\": \"Diego\",\n" +
            "    \"position\": \"CB\",\n" +
            "    \"teamId\": \"idGremio\"\n" +
            "  }\n" +
            "}";

    static String DIEGO_UPDATED_EVENT = "{\n" +
            "  \"event\": \"player_updated\",\n" +
            "  \"player\": {\n" +
            "    \"id\": \"idDiego\",\n" +
            "    \"name\": \"Diego\",\n" +
            "    \"position\": \"CB\",\n" +
            "    \"teamId\": \"idGremio\"\n" +
            "  }\n" +
            "}";

}
