package local.tszolny.cityinfoenricher;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@EnableKafka
public class CityInfoEnricherApplication {

	public static void main(String[] args) {
		SpringApplication.run(CityInfoEnricherApplication.class, args);
	}

}
