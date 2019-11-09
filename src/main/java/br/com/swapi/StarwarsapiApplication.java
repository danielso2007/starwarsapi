package br.com.swapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(value = "br.com.swapi")
@SpringBootApplication
public class StarwarsapiApplication {

	public static void main(String[] args) {
		SpringApplication.run(StarwarsapiApplication.class, args);
	}

}
