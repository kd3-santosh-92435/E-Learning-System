package com.elearning.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateProfileDTO {

	 @Size(min = 2, message = "Name must have at least 2 characters")
	    private String name;

	    @Email(message = "Invalid email format")
	    private String email;

	    // password change is optional
	    private String oldPassword;

	    @Size(min = 6, message = "New password must be at least 6 characters")
	    private String newPassword;
}
