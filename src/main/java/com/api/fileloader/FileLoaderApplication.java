package com.api.fileloader;

import com.api.fileloader.entity.File;
import com.api.fileloader.entity.User;
import com.api.fileloader.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@EnableJpaRepositories("com.api.fileloader.repository")
public class FileLoaderApplication {
	PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	@Autowired
	UserRepository userRepository;

	public static void main(String[] args) {
		SpringApplication.run(FileLoaderApplication.class, args);
	}

	@Bean
	CommandLineRunner init() {
		return args -> {
			User user1 = new User("username1", passwordEncoder.encode("password1"));
			User user2 = new User("username2", passwordEncoder.encode("password2"));
			User user3 = new User("username3", passwordEncoder.encode("password3"));

			userRepository.save(user1);
			userRepository.save(user2);
			userRepository.save(user3);
		};
	}
}
