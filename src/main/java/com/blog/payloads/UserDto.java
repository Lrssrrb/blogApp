package com.blog.payloads;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserDto{

	private int id;
	
	@NotEmpty
	@Size(min = 4,message = "Username must be min of 4 characters.")
	private String name;
	@Email(message = "Email address is not valid.")
	@NotEmpty
	private String email;
	@NotEmpty
	@Size(min = 3,max = 10,message = "Password must be min of 3 characters and max of 10 characters.")
	private String password;
	@NotEmpty
	private String about;
	
	public UserDto(@NotEmpty @Size(min = 4, message = "Username must be min of 4 characters.") String name,
			@Email(message = "Email address is not valid.") @NotEmpty String email,
			@NotEmpty @Size(min = 3, max = 10, message = "Password must be min of 3 characters and max of 10 characters.") String password,
			@NotEmpty String about) {
		super();
		this.name = name;
		this.email = email;
		this.password = password;
		this.about = about;
	}

}
