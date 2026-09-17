package br.com.gabnest.nest_gab_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication
@EnableMongoAuditing
public class NestGabApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(NestGabApiApplication.class, args);
	}

}
