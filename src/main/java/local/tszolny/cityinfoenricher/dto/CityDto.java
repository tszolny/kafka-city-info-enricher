package local.tszolny.cityinfoenricher.dto;

import lombok.Data;

@Data
public class CityDto {
    private String city;

    public CityDto(){
    }

    public CityDto(String city){
        this.city = city;
    }
}
