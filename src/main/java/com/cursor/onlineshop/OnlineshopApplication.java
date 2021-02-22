package com.cursor.onlineshop;

import com.cursor.onlineshop.dtos.CreateAccountDto;
import com.cursor.onlineshop.entities.user.UserPermission;
import com.cursor.onlineshop.services.UserService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.annotation.PostConstruct;
import java.nio.file.AccessDeniedException;
import java.util.Set;

@SpringBootApplication
@EnableSwagger2
@EnableGlobalMethodSecurity(securedEnabled = true)
public class OnlineshopApplication {
	private final UserService userService;
	private final BCryptPasswordEncoder encoder;

	public OnlineshopApplication(UserService userService, BCryptPasswordEncoder encoder) {
		this.userService = userService;
		this.encoder = encoder;
	}

	public static void main(String[] args) {
		SpringApplication.run(OnlineshopApplication.class, args);
	}


//	@PostConstruct
//	public void addUsers() throws AccessDeniedException {
//		var accountDto1 = new CreateAccountDto("igor", encoder.encode("pass"),
//				"jkgfg@fhjf.com", Set.of(UserPermission.ROLE_ADMIN, UserPermission.ROLE_USER));
//		userService.registerWithRole(accountDto1);
//		var accountDto2 = new CreateAccountDto("ivan", encoder.encode("word"),
//				"jdsfhdfhs@fhjf.com", Set.of(UserPermission.ROLE_USER));
//		userService.registerWithRole(accountDto2);
//	}

}
