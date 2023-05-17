package com.hanghae99.maannazan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication()
@EnableJpaAuditing
@EnableCaching
public class MaannaZanApplication {

	public static void main(String[] args) {
		SpringApplication.run(MaannaZanApplication.class, args);
	}

}
