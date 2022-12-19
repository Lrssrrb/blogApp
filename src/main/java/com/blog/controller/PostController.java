package com.blog.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.blog.config.AppConstants;
import com.blog.payloads.PostDto;
import com.blog.payloads.PostResponce;
import com.blog.services.FileService;
import com.blog.services.PostService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
@CrossOrigin("*")
public class PostController {
	
	@Autowired
	private PostService postService;
	
	@Autowired
	private FileService fileService;
	
	
	@Value("${project.image}")
	private String path;// = "images/";
	
	@PostMapping("/posts/category/{categoryId}")
	public ResponseEntity<PostDto>createPost(
			@Valid @RequestBody PostDto postDto,
			@PathVariable Integer categoryId){
		
		PostDto createPost = this.postService.createPost(postDto, categoryId);
		return new ResponseEntity<PostDto>(createPost, HttpStatus.CREATED);
	}
	
	@GetMapping("/posts/user/{userId}")
	public ResponseEntity<List<PostDto>> getPostsByUser(
			@PathVariable Integer userId
			){
		List<PostDto> posts = this.postService.getPostsByUser(userId);
		return new ResponseEntity<List<PostDto>>(posts, HttpStatus.OK);
	}
	
	@GetMapping("/posts/category/{categoryId}")
	public ResponseEntity<List<PostDto>> getPostsByCategory(
			@PathVariable Integer categoryId
			){
		List<PostDto> posts = this.postService.getPostsByCategory(categoryId);
		return new ResponseEntity<List<PostDto>>(posts, HttpStatus.OK);
	}
	
	@GetMapping("/posts/{postId}")
	public ResponseEntity<PostDto> getPostsById(
			@PathVariable Integer postId
			){
		PostDto posts = this.postService.getPostById(postId);
		return new ResponseEntity<PostDto>(posts, HttpStatus.OK);
	}
	
	@GetMapping("/posts")
	public ResponseEntity<List<PostDto>> getAllPosts(){
		List<PostDto> allPosts = this.postService.getAllPost();
		return new ResponseEntity<List<PostDto>>(allPosts, HttpStatus.OK);
	}
	
//	@GetMapping("/pagedPosts/size/{pageSize}/number/{pageNumber}")
//	public ResponseEntity<PostResponce> getAllPagedPosts(
//			@PathVariable Integer pageSize,
//			@PathVariable Integer pageNumber
//			){
//		PostResponce allPosts = this.postService.getPagedPost(pageSize,pageNumber);
//		return new ResponseEntity<PostResponce>(allPosts, HttpStatus.OK);
//	}
	
//	@GetMapping("/pagedPosts/size/{pageSize}/number/{pageNumber}/sortBy/{sortBy}/asc/{sorted}")
//	public ResponseEntity<PostResponce> getAllPaged$SortedPosts(
//			@PathVariable Integer pageSize,
//			@PathVariable Integer pageNumber,
//			@PathVariable String sortBy,
//			@PathVariable Boolean sorted
//			){
//		PostResponce allPosts = this.postService.getAllPaged$SortedPosts(pageSize,pageNumber,sortBy,sorted);
//		return new ResponseEntity<PostResponce>(allPosts, HttpStatus.OK);
//	}
	
	@PutMapping("/posts/{postId}")
	public ResponseEntity<PostDto> updatePostById(
			@PathVariable Integer postId,
			@RequestBody PostDto postDto){
		
		
		
		return new ResponseEntity<PostDto>(this.postService.updatePost(postDto, postId), HttpStatus.OK);
	}
	
	@DeleteMapping("/posts/{postId}")
	public ResponseEntity<String> deletePostById(
			@PathVariable Integer postId){
		
		this.postService.deletePost(postId);
		return new ResponseEntity<String>("Post with id : "+postId+" is now deleted.", HttpStatus.OK);
	}
	
	
//	public PostResponce getPagedPostByUser(int userId,int page_size,int page_Number)
//	@GetMapping("/pagedPosts/user/{userId}/size/{pageSize}/number/{pageNumber}")
//	public ResponseEntity<PostResponce> getPagedPostByUser(
//			@PathVariable int userId,
//			@PathVariable int pageSize,
//			@PathVariable int pageNumber
//			){
//		
//				return new ResponseEntity<PostResponce>(this.postService.getPagedPostByUser(userId, pageSize, pageNumber), HttpStatus.OK);
//	}
	
//	public PostResponce getPagedPostByCategory(int categoryId, int page_size, int page_number)
//	@GetMapping("/pagedPosts/category/{categoryId}/size/{pageSize}/number/{pageNumber}")
//	public ResponseEntity<PostResponce> getPagedPostByCategory(
//			@PathVariable int categoryId,
//			@PathVariable int pageSize,
//			@PathVariable int pageNumber
//			){
//		
//				return new ResponseEntity<PostResponce>(this.postService.getPagedPostByUser(categoryId, pageSize, pageNumber), HttpStatus.OK);
//	}
	
	@GetMapping("/pagedPosts/size/{pageSize}/number/{pageNumber}/sortBy/{sortBy}/asc/{sorted}")
	public ResponseEntity<PostResponce> getEveryThing(
			@RequestParam(value = "pageSize",defaultValue = AppConstants.PAGE_NUMBER,required = false)int pageSize,
			@RequestParam(value = "pageNumber",defaultValue = AppConstants.PAGE_NUMBER,required = false)int pageNumber,
			@RequestParam(value = "sortBy",defaultValue = AppConstants.SORT_BY,required = false)String sortBy,
			@RequestParam(value = "sorted",defaultValue = AppConstants.SORT_DIR,required = false)String sorted
			){
		
		return new ResponseEntity<PostResponce>(this.postService.getEverything(pageSize, pageNumber,sortBy,sorted), HttpStatus.OK);
	}
	
	@GetMapping("/posts/search/{keyword}")
	public ResponseEntity<List<PostDto>> searchPostByTitle(
			@PathVariable("keyword") String keyword
			){
		List<PostDto> searchPost = this.postService.searchPost(keyword);
		return new ResponseEntity<List<PostDto>>(searchPost, HttpStatus.OK);
	}
	
	@PostMapping("/post/image/upload/postid/{postId}")
	public ResponseEntity<PostDto> uploadPostImage(
			@RequestParam("image") MultipartFile image,
			@PathVariable Integer postId
			) throws IOException{
		
		PostDto postDto = this.postService.getPostById(postId);
		String fileName = this.fileService.uploadImage(path, image);
		
		postDto.setImageName(fileName);
		PostDto updatePost = this.postService.updatePost(postDto, postId);
		return new ResponseEntity<PostDto>(updatePost, HttpStatus.OK);
		
	}
	
	
	@PostMapping("/post/image/upload/{categoryId}")
	public ResponseEntity<PostDto> uploadPost(
			@RequestParam("image") MultipartFile image,
//			@Valid @RequestBody PostDto postDto,
			@RequestParam String title,
		    @RequestParam String content,
//		    @RequestParam String imageName,
			@PathVariable Integer categoryId
			) throws IOException{
		System.out.println("bxhbcsch");
//		PostDto postDto = this.postService.getPostById(postId);
		String fileName = this.fileService.uploadImage(path, image);
		PostDto postDto = new PostDto();
		postDto.setImageName(fileName);
		postDto.setContent(content);
		postDto.setTitle(title);
		postDto.setImageName(fileName);
		System.out.println(postDto);
		PostDto updatePost = this.postService.createPost(postDto, categoryId);
		System.out.println(updatePost);
		return new ResponseEntity<PostDto>(updatePost, HttpStatus.OK);
		
	}
	
	
	@GetMapping(value = "/post/image/{imageName}",produces = MediaType.IMAGE_JPEG_VALUE)
	public void ShowImage(
			@PathVariable("imageName") String imageName,
			HttpServletResponse response
			) throws IOException {
		
		InputStream resorce = this.fileService.getResource(path, imageName);
		response.setContentType(MediaType.IMAGE_JPEG_VALUE);
		StreamUtils.copy(resorce,response.getOutputStream());
	}
	
}
