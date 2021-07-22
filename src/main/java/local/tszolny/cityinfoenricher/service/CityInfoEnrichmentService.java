package local.tszolny.cityinfoenricher.service;

import local.tszolny.cityinfoenricher.dto.CityDetailsDto;
import local.tszolny.cityinfoenricher.dto.CityDto;
import local.tszolny.cityinfoenricher.entity.City;
import local.tszolny.cityinfoenricher.repository.CityRepository;
import local.tszolny.cityinfoenricher.rest.CityPopulationRestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import static org.springframework.util.StringUtils.hasLength;

@Service
public class CityInfoEnrichmentService {
    private static final Logger LOG = LoggerFactory.getLogger(CityInfoEnrichmentService.class);

    private final CityRepository cityRepository;
    private final CityPopulationRestClient cityPopulationRestClient;
    private final KafkaTemplate<String, CityDetailsDto> kafkaTemplate;
    private final String outputTopic;

    public CityInfoEnrichmentService(CityRepository cityRepository,
                                     CityPopulationRestClient cityPopulationRestClient,
                                     KafkaTemplate<String, CityDetailsDto> kafkaTemplate,
                                     @Value("${kafka.city.output.topic}") String outputTopic) {
        this.cityRepository = cityRepository;
        this.cityPopulationRestClient = cityPopulationRestClient;
        this.kafkaTemplate = kafkaTemplate;
        this.outputTopic = outputTopic;
    }

    public void enrich(CityDto cityDto) {
        LOG.info("Enriching city {}", cityDto);

        if (cityDto == null || !hasLength(cityDto.getCity())) {
            throw new IllegalArgumentException("city cannot be null or empty.");
        }

        City city = cityRepository.findByName(cityDto.getCity()).orElse(new City());
        city.setPopulation(cityPopulationRestClient.getPopulation(city.getName()));
        kafkaTemplate.send(outputTopic, CityDetailsDto.of(city));
    }
}
