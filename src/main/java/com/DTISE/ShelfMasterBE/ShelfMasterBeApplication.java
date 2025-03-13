package com.DTISE.ShelfMasterBE;

import com.DTISE.ShelfMasterBE.infrastructure.config.JwtConfigProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.core.env.Environment;

import java.util.Map;
import java.util.Properties;

@SpringBootApplication
@EnableConfigurationProperties({JwtConfigProperties.class})
@EnableCaching
public class ShelfMasterBeApplication implements CommandLineRunner {
	@Autowired
	private Environment env;

	public static void main(String[] args) {
		SpringApplication.run(ShelfMasterBeApplication.class, args);
	}
	@Override
	public void run(String... args) {
		System.out.println("========== APPLICATION PROPERTIES ==========");
		System.out.println("Active Profiles: " + String.join(", ", env.getActiveProfiles()));

		Properties properties = System.getProperties();
		Map<String, String> envVars = System.getenv();

		System.out.println("\n== Application Properties ==");
		properties.forEach((key, value) -> {
			if (key.toString().startsWith("spring.mail")) {
				System.out.println(key + " = " + value);
			}
		});

		System.out.println("\n== Environment Variables (Filtered) ==");
		envVars.entrySet().stream()
				.filter(entry -> entry.getKey().contains("SMTP") || entry.getKey().startsWith("spring.mail"))
				.forEach(entry -> System.out.println(entry.getKey() + " = " + entry.getValue()));

		System.out.println("=========================================");
	}
}
