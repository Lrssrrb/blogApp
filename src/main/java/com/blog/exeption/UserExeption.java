package com.blog.exeption;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@ToString
@Getter
@Setter
public class UserExeption extends SecurityException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String message;
}
