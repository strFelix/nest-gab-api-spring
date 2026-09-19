package br.com.gabnest.nest_gab_api.dto.guideline;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class GuidelineRequest {

    @NotBlank(message = "Title is required")
    @Size(min = 3, max = 150, message = "Title must be between 3 and 150 characters")
    private String title;

    @NotBlank(message = "Content is required")
    @Size(min = 10, max = 4000, message = "Content must be between 10 and 4000 characters")
    private String content;

    private String category;
    private String campaign;
}
