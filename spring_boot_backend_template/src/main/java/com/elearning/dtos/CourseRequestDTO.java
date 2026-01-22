package com.elearning.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CourseRequestDTO {
	 @NotBlank(message = "Course title is required")
	    private String title;

	    @NotBlank(message = "Course description is required")
	    private String description;

	    @Min(value = 0, message = "Price must be positive")
	    private double price;
}

