package br.com.ciceroednilson.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("br.com.ciceroednilson.*")
public class SpringbootBatchProcessaCsvApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringbootBatchProcessaCsvApplication.class, args);
	}

}

