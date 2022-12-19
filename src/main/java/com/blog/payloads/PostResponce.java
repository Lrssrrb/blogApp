package com.blog.payloads;

import java.util.List;

import lombok.Data;

@Data
public class PostResponce {
	
	private List<PostDto> content;
	private int pageNumber;
	private int pageSize;
	private Long totalElement;
	private int totalPages;
	private boolean firstPage;
	private boolean lastPage;
	
}
