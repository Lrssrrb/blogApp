package com.blog.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.blog.entities.User;
import com.blog.exeption.ResourceNotFoundException;
import com.blog.repositries.UserRepo;

@Service
public class CustomUserDetailService implements UserDetailsService{

	@Autowired
	private UserRepo userRepo;
	
	@Override
	public User loadUserByUsername(String username) throws UsernameNotFoundException {
		 User user = this.userRepo.findByEmail(username).orElseThrow(()->new ResourceNotFoundException("User", "email", username));
//		 UserDto userDto = mapper.map(user, UserDto.class);
		 return user;
	}

}
