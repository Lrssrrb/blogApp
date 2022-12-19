package com.blog.services;

import com.blog.payloads.JwtAuthRequest;
import com.blog.payloads.JwtAuthResponse;

public interface AuthService {

	JwtAuthResponse login(JwtAuthRequest request) throws Exception;
	
}
