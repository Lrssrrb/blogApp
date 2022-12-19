package com.blog;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
//import org.springframework.web.servlet.config.annotation.CorsRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.blog.config.AppConstants;
import com.blog.entities.Role;
import com.blog.repositries.RoleRepo;

@SpringBootApplication
public class BlogAppApplication implements CommandLineRunner{

	public static void main(String[] args) {
		SpringApplication.run(BlogAppApplication.class, args);
	}
	
	@Autowired
	private RoleRepo roleRepo;
	
//	@Autowired
	
	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}
//
	@Override
	public void run(String... args) throws Exception {
		
		try {
			Role role = new Role();
			role.setId(AppConstants.ADMIN_USER_ID);
			role.setName(AppConstants.ADMIN_USER);
			roleRepo.save(role);
			
			role.setId(AppConstants.NORMAL_USER_ID);
			role.setName(AppConstants.NORMAL_USER);
			roleRepo.save(role);
			
		} catch (Exception e) {
			
		}
		
	}
	
}
