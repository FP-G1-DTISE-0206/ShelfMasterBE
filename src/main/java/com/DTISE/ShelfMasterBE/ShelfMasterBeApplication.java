package com.DTISE.ShelfMasterBE;

import com.DTISE.ShelfMasterBE.infrastructure.config.JwtConfigProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableConfigurationProperties({JwtConfigProperties.class})
@EnableCaching
public class ShelfMasterBeApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShelfMasterBeApplication.class, args);
	}

}
