package com.blog.payloads;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApiResponse {

	String massage;
	Boolean success;
	
	public ApiResponse(String massage) {
		super();
		this.massage = massage;
	}
}
