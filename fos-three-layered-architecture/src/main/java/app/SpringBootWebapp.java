package app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
public class SpringBootWebapp {
	
	public static void main(String[] args) {
		SpringApplication.run(SpringBootWebapp.class, args);
	}
}
