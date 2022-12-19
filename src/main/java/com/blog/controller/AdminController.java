package com.blog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blog.payloads.ApiResponse;
import com.blog.payloads.UserDto;
import com.blog.services.AdminService;

@RestController
@RequestMapping("api/admin")
@CrossOrigin("*")
public class AdminController {

	@Autowired
	private AdminService adminService;
	
	@PutMapping("/new/{id}")
	public ResponseEntity<UserDto> createAdmin(@PathVariable Integer id,@RequestBody ApiResponse input){
//		System.out.println(input);
		UserDto admin = this.adminService.createAdmin(id, input.getMassage());
		return new ResponseEntity<UserDto>(admin,HttpStatus.CREATED);
	}
}
