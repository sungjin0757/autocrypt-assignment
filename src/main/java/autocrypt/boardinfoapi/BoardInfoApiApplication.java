package autocrypt.boardinfoapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class BoardInfoApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(BoardInfoApiApplication.class, args);
	}

}
