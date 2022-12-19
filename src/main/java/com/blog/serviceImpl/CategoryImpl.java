package com.blog.serviceImpl;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.blog.entities.Category;
import com.blog.exeption.ResourceNotFoundException;
import com.blog.exeption.UserExeption;
import com.blog.payloads.CategoryDto;
import com.blog.repositries.CategoryRepo;
import com.blog.services.CategoryService;


@Service
public class CategoryImpl implements CategoryService {

	@Autowired
	private CategoryRepo categoryRepo;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Override
	public CategoryDto createCategory(CategoryDto categoryDto) {
		
		if(!isAdmin())
			throw new UserExeption("Only Admin can create categories.");
		
		Category cat = this.modelMapper.map(categoryDto, Category.class);
		Category addedCat = this.categoryRepo.save(cat);
		return this.modelMapper.map(addedCat, CategoryDto.class);
	}

	@Override
	public CategoryDto updateCategory(CategoryDto categoryDto, Integer categoryId) {
		
		if(!isAdmin())
			throw new UserExeption("Only Admin can update categories.");
		
		this.categoryRepo.findById(categoryId).orElseThrow(()-> new ResourceNotFoundException("Category ","Category Id", categoryId));
		Category cat = this.modelMapper.map(categoryDto, Category.class);
		cat.setCategoryId(categoryId);
		Category Updatedcat = this.categoryRepo.save(cat);
		return this.modelMapper.map(Updatedcat, CategoryDto.class);
	}
	
	@Override
	public void deleteCategory(Integer categoryId) {
		
		if(!isAdmin())
			throw new UserExeption("Only Admin can delete categories.");
		
		Category cat = this.categoryRepo.findById(categoryId)
				.orElseThrow(()-> new ResourceNotFoundException("Category ", "category id", categoryId));
		this.categoryRepo.delete(cat);
	}

	@Override
	public CategoryDto getCategory(Integer categoryId) {
		Category cat = this.categoryRepo.findById(categoryId)
				.orElseThrow(()-> new ResourceNotFoundException("Category ", "category id", categoryId));
		return this.modelMapper.map(cat, CategoryDto.class);
	}

	@Override
	public List<CategoryDto> getCategories() {
		List<Category> categories = this.categoryRepo.findAll();
		List<CategoryDto> categoryDtos = categories.stream()
				.map((post)-> this.modelMapper
				.map(post, CategoryDto.class))
				.collect(Collectors.toList());
//		List<CategoryDto> categoryDtos = new ArrayList<>();
//		categories.forEach(t -> categoryDtos.add(this.modelMapper.map(t, CategoryDto.class)));
		return categoryDtos;
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
