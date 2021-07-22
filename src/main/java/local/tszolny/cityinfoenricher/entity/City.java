package local.tszolny.cityinfoenricher.entity;

import lombok.Data;
import javax.persistence.*;

@Entity
@Table(name = "cities")
@Data
public class City {
    @Id
    @GeneratedValue(generator="city_seq")
    @SequenceGenerator(name="city_seq",sequenceName="city_seq")
    private Long id;

    @Column
    private String name;

    @Column
    private String country;

    @Transient
    private Integer population;

}
