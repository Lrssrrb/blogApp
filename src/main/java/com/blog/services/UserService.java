package com.blog.services;

import java.util.List;

import com.blog.payloads.UserDto;

public interface UserService {

	UserDto RegisterNewUser(UserDto userDto);
	UserDto updateUser(UserDto user,Integer id);
	UserDto getUserById(Integer userId);
	List<UserDto> getAllUsers();
	void deleteUser(Integer userId);
	
}
