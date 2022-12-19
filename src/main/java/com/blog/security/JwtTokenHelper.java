package com.blog.security;

import java.util.Date;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.blog.config.AppConstants;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
@SuppressWarnings("deprecation")
public class JwtTokenHelper {

	public String getUsername(String token) {
		return getClaim(token, Claims::getSubject);
	}

	public Date getExpirationDate(String token) {
		return getClaim(token, Claims::getExpiration);
	}

	public <T> T getClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = getAllClaims(token);
		return claimsResolver.apply(claims);
	}

	private Claims getAllClaims(String token) {
//		SecretKey key = Keys.hmacShaKeyFor(Base64.getDecoder().decode(AppConstants.SECRET));

		return Jwts.parserBuilder().setSigningKey(AppConstants.SECRET).build().parseClaimsJws(token).getBody();

	}

	private Boolean isExpired(String token) {
		final Date expiration = getExpirationDate(token);
		return expiration.before(new Date(System.currentTimeMillis()));
	}

	public String generate(UserDetails userDetails) {
		
		return Jwts.builder().signWith(SignatureAlgorithm.HS256, AppConstants.SECRET)
				.setIssuer(userDetails.getUsername()).setSubject(userDetails.getUsername())
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + AppConstants.JWT_TOKEN_VALIDITY)).compact();

	}

	public Boolean validate(String token, UserDetails userDetails) {
		final String userName = getUsername(token);
		return (userName.equals(userDetails.getUsername()) && !isExpired(token));
	}

}
