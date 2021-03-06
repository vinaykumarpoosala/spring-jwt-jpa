/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.app.blog;

import com.app.blog.models.MyUserDetails;
import com.app.blog.models.Users;
import com.app.blog.service.MyuserDetailsService;
import com.app.blog.util.Constants;
import com.app.blog.util.JWTUtils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;
import java.io.IOException;
import java.util.Collections;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

/**
 *
 * @author 1460344
 */
@Component
public class JwtFilter extends GenericFilterBean {
	
	
	@Autowired
	JWTUtils jwtUtils;
	@Autowired
	MyuserDetailsService customUserDetailsService;
	@Override
	public void doFilter(ServletRequest sr, ServletResponse sr1, FilterChain fc) throws IOException, ServletException {

		try {
			HttpServletRequest httpRequest = (HttpServletRequest) sr;

			String jwt = getJwtFromWebRequest(httpRequest);
			System.out.println(jwt);
			//ah 3 lines lo error anknta
			if (jwt!=null && jwtUtils.validateToken(jwt) && SecurityContextHolder.getContext().getAuthentication() == null) {
				String username = jwtUtils.getUserIdFromJwt(jwt);
				System.out.println(username);
				
				//deeni lopalaki ochindi ante validate ainatte ka
				Users userDetails = customUserDetailsService.getUser(username);
				UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails.getEmail(),userDetails.getPassword(),Collections.EMPTY_LIST);

				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpRequest));
				SecurityContextHolder.getContext().setAuthentication(authentication);
			}

		} catch (Exception ex) {

			logger.error("could not set user authentication in security context", ex);
		}

		fc.doFilter(sr, sr1);
	}

	private String getJwtFromWebRequest(ServletRequest request) {

	    HttpServletRequest httpRequest = (HttpServletRequest) request;

		String bearerToken = httpRequest.getHeader("Authorization");
		if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
			return bearerToken.substring(7);
		}

		return null;
	}

}
