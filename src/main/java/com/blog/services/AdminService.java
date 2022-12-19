package com.blog.services;

import com.blog.payloads.UserDto;

public interface AdminService {

	UserDto createAdmin(Integer id,String password);
}
