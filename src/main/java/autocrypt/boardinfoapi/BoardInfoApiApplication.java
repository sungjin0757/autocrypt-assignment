package autocrypt.boardinfoapi;

import autocrypt.boardinfoapi.security.property.JwtProperty;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@EnableConfigurationProperties({JwtProperty.class})
@SpringBootApplication
public class BoardInfoApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(BoardInfoApiApplication.class, args);
	}

}
