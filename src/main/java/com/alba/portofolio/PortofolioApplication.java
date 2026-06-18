package com.alba.portofolio;

import com.alba.portofolio.entity.AppUser;
import com.alba.portofolio.enums.Role;
import com.alba.portofolio.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@EnableMethodSecurity
public class PortofolioApplication {

	public static void main(String[] args) {
		SpringApplication.run(PortofolioApplication.class, args);
	}


	@Bean
	CommandLineRunner init(UserRepository repo, PasswordEncoder encoder) {
		return args -> {

			if (repo.findByEmail("admin@gmail.com").isEmpty()) {

				AppUser admin = new AppUser();
				admin.setUsername("admin");
				admin.setEmail("admin@gmail.com");
				admin.setPassword(encoder.encode("admin123"));
				admin.setRole(Role.ADMIN);

				repo.save(admin);
			}
		};
	}
}