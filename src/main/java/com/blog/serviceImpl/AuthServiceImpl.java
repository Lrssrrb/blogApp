package com.blog.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import com.blog.entities.User;
import com.blog.exeption.InvalidInput;
import com.blog.exeption.UserExeption;
import com.blog.payloads.JwtAuthRequest;
import com.blog.payloads.JwtAuthResponse;
import com.blog.security.CustomUserDetailService;
import com.blog.security.JwtTokenHelper;
import com.blog.services.AuthService;

@Service
public class AuthServiceImpl implements AuthService{
	
	@Autowired
	CustomUserDetailService customUserDetailService;
	
	@Autowired
	JwtTokenHelper jwtTokenHelper;
	
	@Autowired
	AuthenticationManager authenticationManager;
	
	@Override
	public JwtAuthResponse login(JwtAuthRequest request){
		
		String username = request.getUsername();
		String password = request.getPassword();
		
		try {
		UsernamePasswordAuthenticationToken authenticationToken = 
				new UsernamePasswordAuthenticationToken(username, password);
		
			this.authenticationManager.authenticate(authenticationToken);
			
		} catch (BadCredentialsException e) {
			System.out.println("jkgnfvjn kj");
			throw new InvalidInput("Invalid UserName or password.");
		} catch (AuthenticationException e) {
			throw new InvalidInput("Invalid UserName or password.");
		} 
		catch(Exception e) {
			throw new InvalidInput("please insert a valid password.");
		}
		
		
		User user = this.customUserDetailService.loadUserByUsername(username);
		JwtAuthResponse response = new JwtAuthResponse();
		try {
			String token = this.jwtTokenHelper.generate(user);
			response.setToken(token);
		} catch (Exception e) {
			throw new UserExeption("invalin username");
		}
		
		return response;
	}
	
	
	
}
