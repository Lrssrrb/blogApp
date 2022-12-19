package com.blog.serviceImpl;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.blog.entities.Category;
import com.blog.entities.Post;
import com.blog.entities.User;
import com.blog.exeption.ResourceNotFoundException;
import com.blog.exeption.UserExeption;
import com.blog.payloads.PostDto;
import com.blog.payloads.PostResponce;
import com.blog.repositries.CategoryRepo;
import com.blog.repositries.PostRepo;
import com.blog.repositries.UserRepo;
import com.blog.services.PostService;

@Service
public class PostServiceImpl implements PostService{

	@Autowired
	private PostRepo postRepo;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private UserRepo userRepo;
	
	@Autowired
	private CategoryRepo categoryRepo;
	
	@Override
	public PostDto createPost(PostDto postDto, Integer categoryId) {
		
		String currentUser = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		User user = userRepo.findByEmail(currentUser).get();
		
		Category category = this.categoryRepo.findById(categoryId).orElseThrow(()->new ResourceNotFoundException("Category", "id", categoryId));
		
		Post post = this.modelMapper.map(postDto, Post.class);
		post.setAddedDate(new Date());
		post.setUser(user);
		post.setCategory(category);
		
		Post newPost = this.postRepo.save(post);
		return this.modelMapper.map(newPost, PostDto.class);
	}
	
//	@Override
//	public PostDto createPostwithImg(PostDto postDto, Integer categoryId) {
//		
//		String currentUser = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//		User user = userRepo.findByEmail(currentUser).get();
//		
//		Category category = this.categoryRepo.findById(categoryId).orElseThrow(()->new ResourceNotFoundException("Category", "id", categoryId));
//		
//		Post post = this.modelMapper.map(postDto, Post.class);
//		post.setAddedDate(new Date());
//		post.setUser(user);
//		post.setCategory(category);
//		
//		Post newPost = this.postRepo.save(post);
//		return this.modelMapper.map(newPost, PostDto.class);
//	}

	@Override
	public PostDto updatePost(PostDto postDto, Integer postId) {
		

		Post postGoted = this.postRepo.findById(postId)
				.orElseThrow(()->new ResourceNotFoundException("Post", "post id", postId));
		
		String currentUser = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		if(!isAdmin() || !postGoted.getUser().getEmail().equals(currentUser))
			throw new UserExeption("Only Creater can delete his/her Post.");
		
		Post post = this.modelMapper.map(postDto, Post.class);
		
		
		
		if(post.getContent() == null) {
			post.setContent(postGoted.getContent());
		}
		if(post.getImageName() == null) {
			post.setImageName(postGoted.getImageName());
		}
		if(post.getTitle() == null) {
			post.setTitle(postGoted.getTitle());
		}
		
		post.setId(postId);
		post.setCategory(postGoted.getCategory());
		post.setUser(postGoted.getUser());
		post.setAddedDate(postGoted.getAddedDate());
		
		Post newPost = this.postRepo.save(post);
		return this.modelMapper.map(newPost, PostDto.class);
	}

	@Override
	public void deletePost(Integer postId) {
		
		Post post = this.postRepo.findById(postId)
				.orElseThrow(()->new ResourceNotFoundException("Post", "post id", postId));
		
		String currentUser = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		if(!isAdmin() || !post.getUser().getEmail().equals(currentUser))
			throw new UserExeption("Only Creater can delete his/her Post.");
		
		this.postRepo.delete(post);
	}

	@Override
	public List<PostDto> getAllPost() {
		List<Post> pagePost = this.postRepo.findAll();
		List<PostDto> postDtos = pagePost.stream().map((post)-> this.modelMapper.map(post, PostDto.class)).collect(Collectors.toList());
		return postDtos;
	}

	@Override
	public PostDto getPostById(Integer postId) {
		Post post = this.postRepo.findById(postId)
				.orElseThrow(()->new ResourceNotFoundException("Post", "post id", postId));
		return this.modelMapper.map(post, PostDto.class);
	}

	@Override
	public List<PostDto> getPostsByCategory(Integer categoryId) {
		
		Category cat = this.categoryRepo.findById(categoryId)
				.orElseThrow(()->new ResourceNotFoundException("Category", "category id", categoryId));
		List<Post> posts = this.postRepo.findByCategory(cat);
		
//		List<PostDto> postDtos = new ArrayList<>();
//		posts.forEach((post)->postDtos.add(this.modelMapper.map(post, PostDto.class)));
		List<PostDto> postDtos = posts.stream().map((post)-> this.modelMapper.map(post, PostDto.class)).collect(Collectors.toList());
		return postDtos;
	}

	@Override
	public List<PostDto> getPostsByUser(Integer userId) {
		User user = this.userRepo.findById(userId)
				.orElseThrow(()->new ResourceNotFoundException("User", "user id", userId));
		List<Post> posts = this.postRepo.findByUser(user);
		
		List<PostDto> postDtos = posts.stream().map((post)-> this.modelMapper.map(post, PostDto.class)).collect(Collectors.toList());
		return postDtos;
	}
	
	/*
	{
	@Override
	public PostResponce getPagedPost(int page_size,int page_Number) {
		PageRequest pageble = PageRequest.of(page_Number, page_size);
		Page<Post> pagePost = this.postRepo.findAll(pageble);
		List<Post> allpost = pagePost.getContent();
		List<PostDto> postDtos = allpost.stream().map((post)-> this.modelMapper.map(post, PostDto.class)).collect(Collectors.toList());
		PostResponce postResponce = new PostResponce();
		postResponce.setContent(postDtos);
		postResponce.setPageNumber(pagePost.getNumber());
		postResponce.setPageSize(pagePost.getSize());
		postResponce.setTotalElement(pagePost.getTotalElements());
		postResponce.setTotalPages(pagePost.getTotalPages());
		postResponce.setFirstPage(pagePost.isFirst());
		postResponce.setLastPage(pagePost.isLast());
		return postResponce;
	}
	
	@Override
	public PostResponce getPagedPostByUser(int userId,int page_size,int page_number) {
		PageRequest pageble = PageRequest.of(page_number, page_size);
		
		User user = this.userRepo.findById(userId)
				.orElseThrow(()->new ResourceNotFoundException("User", "user id", userId));
		
		Page<Post> pagePost = this.postRepo.findByUser(user,pageble);
		
		List<Post> allpost = pagePost.getContent();
		List<PostDto> postDtos = allpost.stream().map((post)-> this.modelMapper.map(post, PostDto.class)).collect(Collectors.toList());
		PostResponce postResponce = new PostResponce();
		postResponce.setContent(postDtos);
		postResponce.setPageNumber(pagePost.getNumber());
		postResponce.setPageSize(pagePost.getSize());
		postResponce.setTotalElement(pagePost.getTotalElements());
		postResponce.setTotalPages(pagePost.getTotalPages());
		postResponce.setFirstPage(pagePost.isFirst());
		postResponce.setLastPage(pagePost.isLast());
		return postResponce;
	}

	@Override
	public PostResponce getPagedPostByCategory(int categoryId, int page_size, int page_number) {
		
		PageRequest pageble = PageRequest.of(page_number, page_size);
		
		Category category = this.categoryRepo.findById(categoryId)
				.orElseThrow(()->new ResourceNotFoundException("Category", "category id", categoryId));
		
		Page<Post> pagePost = this.postRepo.findByCategory(category,pageble);
		
		List<Post> allpost = pagePost.getContent();
		List<PostDto> postDtos = allpost.stream().map((post)-> this.modelMapper.map(post, PostDto.class)).collect(Collectors.toList());
		PostResponce postResponce = new PostResponce();
		postResponce.setContent(postDtos);
		postResponce.setPageNumber(pagePost.getNumber());
		postResponce.setPageSize(pagePost.getSize());
		postResponce.setTotalElement(pagePost.getTotalElements());
		postResponce.setTotalPages(pagePost.getTotalPages());
		postResponce.setFirstPage(pagePost.isFirst());
		postResponce.setLastPage(pagePost.isLast());
		return postResponce;
	}

	@Override
	public PostResponce getAllPaged$SortedPosts(int pageSize, int pageNumber, String sortBy,Boolean assending) {
		
		PageRequest pageble = null;
		if(assending)
			pageble = PageRequest.of(pageNumber, pageSize,Sort.by(sortBy));
		else
			pageble = PageRequest.of(pageNumber, pageSize,Sort.by(sortBy).descending());
		Page<Post> pagePost = this.postRepo.findAll(pageble);
		List<Post> allpost = pagePost.getContent();
		List<PostDto> postDtos = allpost.stream().map((post)-> this.modelMapper.map(post, PostDto.class)).collect(Collectors.toList());
		PostResponce postResponce = new PostResponce();
		postResponce.setContent(postDtos);
		postResponce.setPageNumber(pagePost.getNumber());
		postResponce.setPageSize(pagePost.getSize());
		postResponce.setTotalElement(pagePost.getTotalElements());
		postResponce.setTotalPages(pagePost.getTotalPages());
		postResponce.setFirstPage(pagePost.isFirst());
		postResponce.setLastPage(pagePost.isLast());
		return postResponce;
	}
	}
	*/
	
	@Override
	public PostResponce getEverything(int pageSize, int pageNumber, String sortBy,String sortDir) {
		PageRequest pageble = null;
		if(sortDir.equalsIgnoreCase("asc"))
			pageble = PageRequest.of(pageNumber, pageSize,Sort.by(sortBy).ascending());
		else
			pageble = PageRequest.of(pageNumber, pageSize,Sort.by(sortBy).descending());
		Page<Post> pagePost = this.postRepo.findAll(pageble);
		List<Post> allpost = pagePost.getContent();
		List<PostDto> postDtos = allpost.stream().map((post)-> this.modelMapper.map(post, PostDto.class)).collect(Collectors.toList());
		PostResponce postResponce = new PostResponce();
		postResponce.setContent(postDtos);
		postResponce.setPageNumber(pagePost.getNumber());
		postResponce.setPageSize(pagePost.getSize());
		postResponce.setTotalElement(pagePost.getTotalElements());
		postResponce.setTotalPages(pagePost.getTotalPages());
		postResponce.setFirstPage(pagePost.isFirst());
		postResponce.setLastPage(pagePost.isLast());
		return postResponce;
	}

	@Override
	public List<PostDto> searchPost(String keyword) {
		List<Post> posts = this.postRepo.findByTitleContaining(keyword);
		List<PostDto> postDtos = posts.stream().map((post)->this.modelMapper.map(post, PostDto.class)).collect(Collectors.toList());
		return postDtos;
	}
	
	
	private boolean isAdmin() {
		
		Collection<? extends GrantedAuthority> authorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
		
		for (Iterator<? extends GrantedAuthority> iterator = authorities.iterator(); iterator.hasNext();) {
			GrantedAuthority grantedAuthority = (GrantedAuthority) iterator.next();
			if(grantedAuthority.getAuthority().equals("ADMIN_USER"))
				return true;
		}
		
		return false;
	}
	
}
