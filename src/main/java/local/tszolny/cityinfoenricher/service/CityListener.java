package local.tszolny.cityinfoenricher.service;

import local.tszolny.cityinfoenricher.dto.CityDto;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CityListener {
    private static final Logger LOG = LoggerFactory.getLogger(CityListener.class);
    private final CityInfoEnrichmentService enrichmentService;

    @KafkaListener(topics = "${kafka.city.input.topic}", autoStartup = "true")
    void captureCity(CityDto cityDto) {
        LOG.debug("Captured city: {}", cityDto);
        enrichmentService.enrich(cityDto);
    }
}
