package com.boom.auth_service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

	// ⚠️ Must be at least 32 characters
	private static final String SECRET = "@ansari@muzammil@ahmed@mohammed@salim";

	private Key getSigningKey() {
		return Keys.hmacShaKeyFor(SECRET.getBytes());
	}

	// 🟢 Generate Token
	public String generateToken(String email) {
		return Jwts.builder().setSubject(email).setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 4)) // 4 hour
				.signWith(getSigningKey()).compact();
	}

	// 🟡 Validate & extract email
	public String extractEmail(String token) {
		return Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token).getBody().getSubject();
	}

	// 🔴 Check token validity
	public boolean isTokenValid(String token) {
		try {
			extractEmail(token);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}
