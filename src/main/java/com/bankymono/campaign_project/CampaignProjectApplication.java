package com.bankymono.campaign_project;

import com.bankymono.campaign_project.security.model.ERole;
import com.bankymono.campaign_project.security.model.Role;
import com.bankymono.campaign_project.security.model.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

@SpringBootApplication
public class CampaignProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(CampaignProjectApplication.class, args);
	}

	@Bean
	@Profile("!test") // Don't run this in test profiles if you have them
	CommandLineRunner initRoles(RoleRepository roleRepository) {
		return args -> {
			if (roleRepository.findByName(ERole.ROLE_USER).isEmpty()) {
				roleRepository.save(new Role(ERole.ROLE_USER));
			}
			if (roleRepository.findByName(ERole.ROLE_MODERATOR).isEmpty()) {
				roleRepository.save(new Role(ERole.ROLE_MODERATOR));
			}
			if (roleRepository.findByName(ERole.ROLE_ADMIN).isEmpty()) {
				roleRepository.save(new Role(ERole.ROLE_ADMIN));
			}
		};
	}

}
