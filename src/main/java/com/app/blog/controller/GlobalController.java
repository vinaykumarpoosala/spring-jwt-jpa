package com.app.blog.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.blog.dto.LoginDto;
import com.app.blog.dto.RegisterUserDTO;
import com.app.blog.models.MyUserDetails;
import com.app.blog.models.Users;
import com.app.blog.repository.UserRepository;
import com.app.blog.util.JWTUtils;

@RestController
@RequestMapping("/")
public class GlobalController {

	
	
	@Autowired
	AuthenticationManager authenticationManager;
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private JWTUtils jwtUtils;
	
	@PostMapping("register")
	public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterUserDTO dto, BindingResult result) {

		ResponseEntity<?> error = mapForErrors(result);
		if(error != null) return error;
		
		Users user = new Users();
		user.setPassword(dto.getPassword());
		user.setEmail(dto.getEmail());
		user.setUserName(dto.getName());
		
		userRepo.save(user);
		
		return ResponseEntity.ok("User Registered");

	}
	
	
	
	@PostMapping("login")
	public ResponseEntity<?> login(@Valid @RequestBody LoginDto dto) {
		
		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword()));
		
		Users user = userRepo.findByEmail(dto.getEmail());
		
		String token = "Bearer "+jwtUtils.CreateJWTToken(user);
		
		return new ResponseEntity<String>(token,HttpStatus.OK);
		
		
		
	}
	
	
	

	public ResponseEntity<?> mapForErrors(BindingResult result) {

		if (result.hasErrors()) {
			
			
			return new ResponseEntity<String>("email Email cannot be blank", HttpStatus.OK);
		}

		return null;

	}
}
