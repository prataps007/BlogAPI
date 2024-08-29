package com.example.blog_app_apis;

import com.example.blog_app_apis.config.AppConstants;
import com.example.blog_app_apis.entities.Role;
import com.example.blog_app_apis.repositories.RoleRepo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;


@SpringBootApplication
@EnableCaching
@EnableScheduling
public class BlogAppApisApplication implements CommandLineRunner {

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private RoleRepo roleRepo;

	public static void main(String[] args) {

		SpringApplication.run(BlogAppApisApplication.class, args);
	}

	@Bean
	public ModelMapper modelMapper(){
		return new ModelMapper();
	}


	@Override
	public void run(String... args) throws Exception {
		// when creating a new user through mongodb compass or atlas -> we have to store encoded password -> otherwise we cant verify that user's password
		// on postman -> so get the encoded form of the password which you want to create and then use it
		System.out.println("Encoded password: " +passwordEncoder.encode("anand"));

		try{
			Role role = new Role();
			role.setId(AppConstants.ADMIN_USER);  // has authority to perform any operation
			role.setName("ROLE_ADMIN");

			Role role1 = new Role();
			role1.setId(AppConstants.NORMAL_USER);   // has authority to perform operations on his own profile only
			role1.setName("ROLE_NORMAL");

			Role role2 = new Role();
			role2.setId(AppConstants.EDITOR_USER);  // has authority to edit posts but not delete them
			role2.setName("ROLE_EDITOR");

			// CAN MANAGE COMMENTS
			Role role3 = new Role();
			role3.setId(AppConstants.MODERATOR_USER);   // has authority to manage  comments
			role3.setName("ROLE_MODERATOR");

			List<Role> roles = List.of(role,role1,role2,role3);

			List<Role> result = roleRepo.saveAll(roles);

			result.forEach(r ->{
				System.out.println(r.getName());
			});
		}
		catch (Exception e){
			e.printStackTrace();
		}
	}

}
