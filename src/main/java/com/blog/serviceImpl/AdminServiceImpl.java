package com.blog.serviceImpl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.blog.config.AppConstants;
import com.blog.entities.User;
import com.blog.exeption.InvalidInput;
import com.blog.payloads.UserDto;
import com.blog.repositries.RoleRepo;
import com.blog.repositries.UserRepo;
import com.blog.services.AdminService;

@Service
public class AdminServiceImpl implements AdminService{

	BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
	
	@Autowired
	UserRepo userRepo;
	
	@Autowired
	RoleRepo roleRepo;
	
	@Autowired
	ModelMapper modelMapper;
	
	@Override
	public UserDto createAdmin(Integer id, String password) {
		if(!bCryptPasswordEncoder.matches(password, AppConstants.ADMIN_PASS))
			throw new InvalidInput("Don't try to do it");
		
		User user = userRepo.findById(id).get();
		user.getRoles().add(roleRepo.findById(AppConstants.ADMIN_USER_ID).get());
		userRepo.save(user);
		return this.modelMapper.map(user, UserDto.class);
	}

}
