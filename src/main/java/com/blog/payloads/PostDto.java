package com.blog.payloads;

import java.util.Date;
import java.util.List;


import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class PostDto {
	
	private int id;
	
	@NotEmpty
	@Size(min = 4,message = "Title must be min of 4 characters.")
	private String title;
	
	@NotEmpty
	@Size(min = 10,message = "Content must be min of 10 characters.")
	private String content;
	
	@NotEmpty
	@Size(min = 4,message = "Image name must be min of 4 characters.")
	private String imageName;
	
	private Date addedDate;
	private CategoryDto category;
	private UserDto user;
	
	private List<CommentDto> comments;
	
}
