package local.tszolny.cityinfoenricher.dto;

import local.tszolny.cityinfoenricher.entity.City;
import lombok.Data;

@Data
public class CityDetailsDto {

    private String city;
    private String country;
    private Integer population;

   public static CityDetailsDto of(City city){
        CityDetailsDto cityDetailsDto = new CityDetailsDto();
        cityDetailsDto.setCity(city.getName());
        cityDetailsDto.setCountry(city.getCountry());
        cityDetailsDto.setPopulation(city.getPopulation());
        return cityDetailsDto;
    }

}
