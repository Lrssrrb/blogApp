package com.blog.services;

import java.util.List;


import com.blog.payloads.PostDto;
import com.blog.payloads.PostResponce;

public interface PostService {

	PostDto createPost(PostDto postDto,Integer categoryId);
//	PostDto createPost(String );
//	public PostDto createPostwithImg(PostDto postDto, Integer categoryId);
	PostDto updatePost(PostDto postDto,Integer postId);
	void deletePost(Integer postId);
	List<PostDto> getAllPost();
	PostDto getPostById(Integer postId);
	List<PostDto> getPostsByCategory(Integer categoryId);
	List<PostDto> getPostsByUser(Integer userId);
//	PostResponce getPagedPost(int page_size,int page_number);
//	PostResponce getPagedPostByUser(int userId,int page_size,int page_number);
//	PostResponce getPagedPostByCategory(int categoryId,int page_size,int page_number);
//	PostResponce getAllPaged$SortedPosts(int pageSize, int pageNumber, String sortBy,Boolean assending);
	PostResponce getEverything(int pageSize, int pageNumber, String sortBy,String sortDir);
	List<PostDto> searchPost(String keyword);
//	PostDto createPostwithImg(PostDto postDto, Integer userId, Integer categoryId,MultipartFile image) throws IOException;
	
}
