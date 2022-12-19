package com.blog.services;

import com.blog.payloads.CommentDto;

public interface CommentService {

	CommentDto createComment(CommentDto commentDto,Integer postId);
	CommentDto editComment(Integer commentId,String newComment);
	void deleteComment(Integer commentId);
	
}
