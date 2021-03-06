/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.app.blog.util;

import com.app.blog.models.Users;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

import java.util.Date;

import org.springframework.stereotype.Component;

/**
 *
 * @author 1460344
 */
@Component
public class JWTUtils {

    public String CreateJWTToken(Users user) {
        
        Claims claims= Jwts.claims();
        claims.put("name", user.getUserName());
        claims.put("email", user.getEmail());
        claims.put("user_id", user.getUserId());
        claims.setSubject("MY Blog");
        claims.setIssuedAt(new Date());
        
        String token = Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS256, Constants.JWT_SECRET)
                .compact();
        
        return token;
    }
    
    public boolean validateToken(String token) {
		try {
			Jwts.parser().setSigningKey(Constants.JWT_SECRET).parseClaimsJws(token);
			return true;
		} catch (SignatureException ex) {
			System.out.println("Inavalid JWT Signature");
		} catch (MalformedJwtException ex) {
			System.out.println("Inavlid JWT Token");
		} catch (ExpiredJwtException ex) {
			System.out.println("JWT Token expired");
		} catch (UnsupportedJwtException ex) {
			System.out.println("Unsupported Jwt Token");
		} catch (IllegalArgumentException ex) {
			System.out.println("JWT Claims string empty");
		}
		return false;
		
		//validate aithundi
	}

	public String getUserIdFromJwt(String token) {
		Claims claims = Jwts.parser().setSigningKey(Constants.JWT_SECRET).parseClaimsJws(token).getBody();
		String id = (String) claims.get("email");
		return id;
	}
}
