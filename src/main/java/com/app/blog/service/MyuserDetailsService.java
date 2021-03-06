package com.app.blog.service;

import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.app.blog.models.MyUserDetails;
import com.app.blog.models.Users;
import com.app.blog.repository.UserRepository;

@Service
public class MyuserDetailsService implements UserDetailsService {

	@Autowired
	private UserRepository repo;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		Users user = getUser(username);
		
		MyUserDetails userDetails = new MyUserDetails(user);
		
		return userDetails;
	}
	
	
	public Users getUser(String userName) {
		
		Users user = repo.findByEmail(userName);
		
		return user;
	}

	
}
