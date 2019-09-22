package com.xcodeassociated.cloud.gateway.security.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xcodeassociated.cloud.gateway.security.dto.UserQueryResponseServiceDto;
import com.xcodeassociated.cloud.gateway.security.model.UserSubject;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JWTUtil implements Serializable {

	private static final long serialVersionUID = 1L;
	private String secret;
	private String expirationTime;

    public JWTUtil(@Value("${springbootwebfluxjjwt.jjwt.secret}") String secret,
                   @Value("${springbootwebfluxjjwt.jjwt.expiration}") String expirationTime) {
        this.secret = secret;
        this.expirationTime = expirationTime;
    }

    public Claims getAllClaimsFromToken(String token) {
		return Jwts.parser()
            .setSigningKey(Base64.getEncoder()
                .encodeToString(this.secret.getBytes()))
            .parseClaimsJws(token)
            .getBody();
	}

	public String getUsernameFromToken(String token) {
		return getAllClaimsFromToken(token).getSubject();
	}

	public Date getExpirationDateFromToken(String token) {
		return getAllClaimsFromToken(token).getExpiration();
	}

	private Boolean isTokenExpired(String token) {
		final Date expiration = getExpirationDateFromToken(token);
		return expiration.before(Date.from(Instant.now()));
	}

	public String generateToken(UserQueryResponseServiceDto userQueryResponseServiceDto, UserSubject userSubject) throws JsonProcessingException {
		Map<String, Object> claims = new HashMap<>();
		claims.put("role", userQueryResponseServiceDto.getRoles());
		return doGenerateToken(claims, userSubject);
	}

	private String doGenerateToken(Map<String, Object> claims, UserSubject userSubject) throws JsonProcessingException {
		final Long expirationTimeLong = Long.parseLong(this.expirationTime);
        final ObjectMapper objectMapper = new ObjectMapper();
		final Date createdDate = Date.from(Instant.now());
		final Date expirationDate = new Date(createdDate.getTime() + expirationTimeLong * 1000);
		return Jwts.builder()
				.setClaims(claims)
				.setSubject(objectMapper.writeValueAsString(userSubject))
				.setIssuedAt(createdDate)
				.setExpiration(expirationDate)
				.signWith(SignatureAlgorithm.HS512, Base64.getEncoder().encodeToString(this.secret.getBytes()))
				.compact();
	}

	public Boolean validateToken(String token) {
		return !isTokenExpired(token);
	}

}
