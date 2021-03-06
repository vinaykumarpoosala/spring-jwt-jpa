package com.app.blog.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/api")
public class PostsController {

	@RequestMapping("/test")
	public String test() {
		
		return "SuccessFull";
	}
}
