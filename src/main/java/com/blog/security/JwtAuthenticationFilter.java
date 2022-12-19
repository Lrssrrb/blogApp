package com.blog.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.blog.exeption.UserExeption;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter{

	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private JwtTokenHelper jwtTokenHelper;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException, UserExeption{
		
		String requestToken = request.getHeader("Authorization");
		
		String username = null;
		
		String token = null;
		
		if(requestToken != null && requestToken.startsWith("Bearer ")) {
			
			token = requestToken.substring(7);
//			token += System.currentTimeMillis(); 
			
			try {
				username = this.jwtTokenHelper.getUsername(token);
			} catch (IllegalArgumentException e) {
//				throw new UserExeption("Unable to get Jwt token");
				System.out.println("Unable to get Jwt token");
			} catch (ExpiredJwtException e) {
//				throw new UserExeption("Jwt token has expired.");
				System.out.println("Jwt token has expired.");
			} catch (MalformedJwtException e) {
//				throw new UserExeption("invalid jwt tokan");
				System.out.println("invalid jwt");
			} catch (Exception e) {
				 System.out.println("please chack the token.");
			}
			
		}else {
			System.out.println("Jwt token does not begin with 'Bearer'.");
		}
		
		if(username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			
			UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
			
			if(this.jwtTokenHelper.validate(token, userDetails)) {
				
				UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
						new UsernamePasswordAuthenticationToken(userDetails.getUsername(), null , userDetails.getAuthorities());
				
				usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				
				SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
				
			}else {
				System.out.println("Invalid jwt token");
			}
			
		}else {
			System.out.println("userName is null or context is not null.");
		}
		
		filterChain.doFilter(request, response);
	}
}
