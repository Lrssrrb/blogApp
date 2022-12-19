package com.blog.payloads;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CategoryDto {

	private Integer categoryId;
	@NotEmpty
	@Size(min = 4,message = "categoryTitle can not be less then 4 charactors")
	private String categoryTitle;
	@Size(min = 10,message = "categoryDescription can not be less then 4 charactors")
	private String categoryDescription;
}
