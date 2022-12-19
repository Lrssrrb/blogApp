package com.blog.repositries;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import com.blog.entities.Category;
import com.blog.entities.Post;
import com.blog.entities.User;

public interface PostRepo extends JpaRepository<Post, Integer>{

	List<Post> findByUser(User user);
	List<Post> findByCategory(Category user);
	Page<Post> findByUser(User user,PageRequest pageble);
	Page<Post> findByCategory(Category user,PageRequest pageble);
	List<Post> findByTitleContaining(String title);
	
}
