package com.blog.serviceImpl;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.blog.config.AppConstants;
import com.blog.entities.User;
import com.blog.payloads.UserDto;
import com.blog.repositries.RoleRepo;
import com.blog.repositries.UserRepo;
import com.blog.services.UserService;
import com.blog.exeption.InvalidInput;
import com.blog.exeption.ResourceNotFoundException;
import com.blog.exeption.UserExeption;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepo userRepo;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	private RoleRepo roleRepo;

	@Override
	public UserDto updateUser(UserDto userDto, Integer userid) {

		User user = this.userRepo.findById(userid)
				.orElseThrow(()-> new ResourceNotFoundException("User"," id ",userid));
		
		String currentUser = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		if(!isAdmin() || !user.getEmail().equals(currentUser))
			throw new UserExeption("Only Creater can update his/her details.");
		
		user.setName(userDto.getName());
		user.setEmail(userDto.getEmail());
		user.setAbout(userDto.getAbout());
		
		user.setPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));
		User updatedUser = this.userRepo.save(user);
		return this.userToDto(updatedUser);
	}

	@Override
	public UserDto getUserById(Integer userId) {
		
		User user = this.userRepo.findById(userId)
				.orElseThrow(()-> new ResourceNotFoundException("User"," id ",userId));
		
		return this.userToDto(user);
	}

	@Override
	public List<UserDto> getAllUsers() {
		List<User> users = this.userRepo.findAll();
		List<UserDto> userDtos = users.stream().map(user -> this.userToDto(user)).collect(Collectors.toList());
		return userDtos;
	}

	@Override
	public void deleteUser(Integer userId) {
		
		User user = this.userRepo.findById(userId)
				.orElseThrow(()-> new ResourceNotFoundException("User"," id ",userId));
		
		String currentUser = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		if(!isAdmin() || !user.getEmail().equals(currentUser))
			throw new UserExeption("Only Creater can delete his/her account.");
		
		this.userRepo.delete(user);
	}

	@Override
	public UserDto RegisterNewUser(UserDto userDto) {
		User user = this.modelMapper.map(userDto, User.class);
		user.getRoles().add(roleRepo.findById(AppConstants.NORMAL_USER_ID).get());
		user.setPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));
		try {
			User newUser = this.userRepo.save(user);
			return this.modelMapper.map(newUser, UserDto.class);
		} catch (Exception e) {
			throw new InvalidInput("Email is already exist.");
		}
	}
	
	private UserDto userToDto(User user) {
		UserDto dto = this.modelMapper.map(user, UserDto.class);
		return dto;
	}
	
	private boolean isAdmin() {
		
		Collection<? extends GrantedAuthority> authorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
		
		for (Iterator<? extends GrantedAuthority> iterator = authorities.iterator(); iterator.hasNext();) {
			GrantedAuthority grantedAuthority = (GrantedAuthority) iterator.next();
			if(grantedAuthority.getAuthority().equals("ADMIN_USER"))
				return true;
		}
		
		return false;
	}
	
}
