package local.tszolny.cityinfoenricher;

import com.github.tomakehurst.wiremock.client.WireMock;
import local.tszolny.cityinfoenricher.dto.CityDto;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;

import java.util.Arrays;
import java.util.Properties;

import static org.apache.kafka.clients.consumer.ConsumerConfig.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(properties = "spring.kafka.bootstrap-servers=${spring.embedded.kafka.brokers}")
@DirtiesContext
@EmbeddedKafka(topics = {"input_topic", "output_topic"}, partitions = 1, ports = 9092, brokerPropertiesLocation = "classpath:application.properties")
@Sql("classpath:cities_with_countries.sql")
@AutoConfigureWireMock(port = 8081)
class CityInfoEnrichmentServiceIntegrationTest {

    @Autowired
    private KafkaTemplate<String, CityDto> kafkaTemplate;

    @Autowired
    private EmbeddedKafkaBroker kafkaBroker;

    @Test
    void shouldPublishToKafka() {
        //given
        String city = "KLUCZBORK";
        CityDto cityDto = new CityDto(city);
        ProducerRecord<String, CityDto> record = new ProducerRecord<>("input_topic", cityDto);
        WireMock.stubFor(WireMock.get(WireMock.urlEqualTo("/citypopulations/" + city))
                .willReturn(WireMock.aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"city\":\"KLUCZBORK\",\"population\":25000}")));

        //when
        kafkaTemplate.send(record);

        //then
        final ConsumerRecords<String, String> records = KafkaTestUtils.getRecords(consumer(), 3000);
        assertThat(records).hasSize(1);
        String value = records.iterator().next().value();
        assertThat(value).isEqualTo("{\"city\":\"KLUCZBORK\",\"country\":\"POLAND\",\"population\":25000}");
    }

    private Consumer<String, String> consumer() {
        Properties props = new Properties();
        props.setProperty(BOOTSTRAP_SERVERS_CONFIG, kafkaBroker.getBrokersAsString());
        props.setProperty(GROUP_ID_CONFIG, "test");
        props.setProperty(ENABLE_AUTO_COMMIT_CONFIG, "true");
        props.setProperty(AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000");
        props.setProperty(KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        props.setProperty(VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        props.setProperty(AUTO_OFFSET_RESET_CONFIG, "earliest");

        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Arrays.asList("output_topic"));

        return consumer;
    }
}