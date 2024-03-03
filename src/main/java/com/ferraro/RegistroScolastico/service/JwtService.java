package com.ferraro.RegistroScolastico.service;

import org.springframework.stereotype.Service;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;

@Service
public class JwtService {
	
	private final static String SECRET_KEY = "b0abc1q8xk38n6g4l9g5y20mm5ez9tkguat9pv7szu9ed97e2cbcb7a4e6548927c3";
	
	private Key getSigningKey() {
		byte[] key = Decoders.BASE64.decode(SECRET_KEY);
		return Keys.hmacShaKeyFor(key);
	}
	
	public String generateToken(Map<String, Object> claims, UserDetails userDetails) {
		return Jwts
				.builder()
				.setClaims(claims)
				.setSubject(userDetails.getUsername())
				.claim("authorities", userDetails.getAuthorities())
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 *24))
				.signWith(getSigningKey(), SignatureAlgorithm.HS256)
				.compact();
	}
	
	public String generateToken(UserDetails userDetails) {
		return generateToken(Collections.emptyMap(), userDetails);
	}
	
	private Claims extractAllClaims(String jwt) {
		return Jwts
				.parserBuilder()
				.setSigningKey(getSigningKey())
				.build()
				.parseClaimsJws(jwt)
				.getBody();
	}
	
	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}
	
	public String extractUsername(String token) {
		return extractClaim(token, Claims::getSubject);
	}
	
	public Boolean isTokenValid(String token, UserDetails userDetails) {
		final String username = extractUsername(token);
		return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}

	private boolean isTokenExpired(String token) {
		
		return extractExpiration(token).before(new Date());
	}

	private Date extractExpiration(String token) {
		
		return extractClaim(token, Claims::getExpiration);
	}

}
