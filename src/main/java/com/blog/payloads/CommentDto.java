package com.blog.payloads;


import lombok.Data;

@Data
public class CommentDto {

	private int id;
	private int userId;
	private String content;
	
	
}
