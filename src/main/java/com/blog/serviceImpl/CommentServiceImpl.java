package com.blog.serviceImpl;

import java.util.Collection;
import java.util.Iterator;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.blog.entities.Comment;
import com.blog.entities.Post;
import com.blog.entities.User;
import com.blog.exeption.ResourceNotFoundException;
import com.blog.exeption.UserExeption;
import com.blog.payloads.CommentDto;
import com.blog.repositries.CommentRepo;
import com.blog.repositries.PostRepo;
import com.blog.repositries.UserRepo;
import com.blog.services.CommentService;

@Service
public class CommentServiceImpl implements CommentService{

	@Autowired
	private PostRepo postRepo;
	
	@Autowired
	private UserRepo userRepo;
	
	@Autowired
	private CommentRepo commentRepo;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Override
	public CommentDto createComment(CommentDto commentDto, Integer postId) {
		Post post = this.postRepo.findById(postId).orElseThrow(()->new ResourceNotFoundException("post", "id", postId));
//		User user = this.userRepo.findById(userId).orElseThrow(()->new ResourceNotFoundException("user", "id", userId));
//		Current logedIn = currentRepo.findById(0).orElseThrow(()->new InvalidInput("please login first."));
//		String userEmail = logedIn.getUserEmail();
		
		String currentUser = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		User user = userRepo.findByEmail(currentUser).get();
		
		Comment comment = this.modelMapper.map(commentDto, Comment.class);
		
		comment.setPost(post);
		comment.setUser(user);
		
		this.commentRepo.save(comment);
		return this.modelMapper.map(comment, CommentDto.class);
	}

	@Override
	public CommentDto editComment(Integer commentId, String newComment) {
		Comment comment = this.commentRepo.findById(commentId).orElseThrow(()->new ResourceNotFoundException("comment", "id", commentId));
		
		String currentUser = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		if(!isAdmin() || !comment.getUser().getEmail().equals(currentUser))
			throw new UserExeption("Only Creater can edit his/her comment.");
		
		comment.setContent(newComment);
		Comment save = this.commentRepo.save(comment);
		return this.modelMapper.map(save, CommentDto.class);
	}

	@Override
	public void deleteComment(Integer commentId) {
		Comment comment = this.commentRepo.findById(commentId).orElseThrow(()->new ResourceNotFoundException("comment", "id", commentId));
		
		String currentUser = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		
		if(!isAdmin() || !comment.getUser().getEmail().equals(currentUser))
			throw new UserExeption("Only Creater can delete his/her comment.");
		
		this.commentRepo.delete(comment);
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
