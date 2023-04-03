package com.hanghae99.maannazan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing

public class MaannaZanApplication {

	public static void main(String[] args) {
		SpringApplication.run(MaannaZanApplication.class, args);
	}

}
