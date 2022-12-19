package com.blog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blog.payloads.JwtAuthRequest;
import com.blog.payloads.JwtAuthResponse;
import com.blog.payloads.UserDto;
import com.blog.serviceImpl.AuthServiceImpl;
import com.blog.services.UserService;

@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin("*")
public class AuthController {
	
	@Autowired
	private AuthServiceImpl authServiceImpl;
	
	@Autowired
	private UserService userService;
	
	
	@PostMapping("/login")
	public ResponseEntity<JwtAuthResponse> createToken(
		@RequestBody JwtAuthRequest request
	) throws Exception{
		JwtAuthResponse response = authServiceImpl.login(request);
		return new ResponseEntity<JwtAuthResponse>(response, HttpStatus.OK);
	}
	
	
	
	@PostMapping("/register")
	public ResponseEntity<UserDto> registerUser(@RequestBody UserDto userDto){
		UserDto registerdUser = this.userService.RegisterNewUser(userDto);
		return new ResponseEntity<UserDto>(registerdUser, HttpStatus.CREATED);
	}
	
}
