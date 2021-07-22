package local.tszolny.cityinfoenricher.rest;

import local.tszolny.cityinfoenricher.dto.CityPopulationDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class CityPopulationRestClient {
    private final RestTemplate restTemplate;
    private final String getCityPopulationUrl;

    public CityPopulationRestClient(@Value("${rest.get.citypopulation.url}") String getCityPopulationUrl){
        restTemplate = new RestTemplate();
        this.getCityPopulationUrl = getCityPopulationUrl;
    }

    public Integer getPopulation(String name){
        String url = getCityPopulationUrl + "/" + name;
        ResponseEntity<CityPopulationDto> responseEntity = restTemplate.getForEntity(url, CityPopulationDto.class);
        if(HttpStatus.OK.equals(responseEntity.getStatusCode())){
            return responseEntity.getBody().getPopulation();
        } else{
            throw new RuntimeException("Can't load city population");
        }
    }

}
