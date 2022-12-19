package com.blog.repositries;

import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.stereotype.Repository;

import com.blog.entities.Category;

public interface CategoryRepo extends JpaRepository<Category, Integer>{

}
