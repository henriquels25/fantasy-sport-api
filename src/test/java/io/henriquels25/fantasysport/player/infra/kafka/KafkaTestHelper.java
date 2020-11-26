package io.henriquels25.fantasysport.player.infra.kafka;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.utils.KafkaTestUtils;

import java.util.Map;

public class KafkaTestHelper {
    public static Consumer<String, String> createConsumer(EmbeddedKafkaBroker embeddedKafka, String topic) {
        Map<String, Object> consumerProps = KafkaTestUtils.consumerProps(RandomStringUtils.randomAlphabetic(10),
                "true", embeddedKafka);
        consumerProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
        ConsumerFactory<String, String> cf = new DefaultKafkaConsumerFactory<>(consumerProps);
        Consumer<String, String> consumer = cf.createConsumer();
        embeddedKafka.consumeFromAnEmbeddedTopic(consumer, topic);
        KafkaTestUtils.getRecords(consumer, 10).records(topic);
        return consumer;
    }

    public static ConsumerRecord<String, String> getNextRecord(Consumer<String, String> consumer) {
        return KafkaTestUtils.getRecords(consumer).records("player-events-v1").iterator().next();
    }
}
