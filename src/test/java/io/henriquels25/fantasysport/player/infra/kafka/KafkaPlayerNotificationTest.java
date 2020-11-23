package io.henriquels25.fantasysport.player.infra.kafka;

import io.henriquels25.fantasysport.annotations.IntegrationTest;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;

import static io.henriquels25.fantasysport.player.factories.PlayerFactory.diego;
import static io.henriquels25.fantasysport.player.infra.kafka.KafkaTestHelper.createConsumer;
import static io.henriquels25.fantasysport.player.infra.kafka.KafkaTestHelper.getNextRecord;
import static io.henriquels25.fantasysport.player.infra.kafka.PlayerKafkaTestData.DIEGO_CREATED_EVENT;
import static io.henriquels25.fantasysport.player.infra.kafka.PlayerKafkaTestData.DIEGO_UPDATED_EVENT;
import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;

@SpringBootTest(classes = {KafkaAutoConfiguration.class, JacksonAutoConfiguration.class})
@ActiveProfiles("test")
@Import({KafkaPlayerNotification.class, KafkaPlayerMapperImpl.class})
@EmbeddedKafka(partitions = 1,
        topics = {"player-events-v1"},
        bootstrapServersProperty = "spring.kafka.bootstrap-servers")
class KafkaPlayerNotificationTest {

    @Autowired
    private EmbeddedKafkaBroker embeddedKafka;

    @Autowired
    private KafkaPlayerNotification playerNotification;

    @IntegrationTest
    void shouldSendAPlayerCreatedEvent() {
        Consumer<String, String> consumer = createConsumer(embeddedKafka,
                "player-events-v1");

        playerNotification.notificateCreated(diego()).block();

        ConsumerRecord<String, String> message = getNextRecord(consumer);
        assertThatJson(message.value()).isEqualTo(DIEGO_CREATED_EVENT);
    }

    @IntegrationTest
    void shouldSendAPlayerUpdatedEvent() {
        Consumer<String, String> consumer = createConsumer(embeddedKafka,
                "player-events-v1");

        playerNotification.notificateUpdated(diego()).block();

        ConsumerRecord<String, String> message = getNextRecord(consumer);

        assertThatJson(message.value()).isEqualTo(DIEGO_UPDATED_EVENT);
    }


}