package io.henriquels25.fantasysport.player;

class AcceptanceTestKafkaTestData {

    static String ACCEPTANCE_TEST_CREATED_EVENT = "{\n" +
            "  \"event\": \"player_created\",\n" +
            "  \"player\": {\n" +
            "    \"id\": \"${json-unit.ignore}\",\n" +
            "    \"name\": \"Diego\",\n" +
            "    \"position\": \"CB\",\n" +
            "    \"teamId\": \"idGremio\"\n" +
            "  }\n" +
            "}";

    static String ACCEPTANCE_TEST_UPDATED_EVENT = "{\n" +
            "  \"event\": \"player_updated\",\n" +
            "  \"player\": {\n" +
            "    \"id\": \"${json-unit.ignore}\",\n" +
            "    \"name\": \"updatedName\",\n" +
            "    \"position\": \"CF\",\n" +
            "    \"teamId\": \"idInternacional\"\n" +
            "  }\n" +
            "}";
}
