package de.fherold.skillswap.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Request DTO for creating or updating a skill
 */
@Schema(description = "Request object for creating or updating a skill")
public record SkillRequestDto(
    
    @Schema(
        description = "The title of the skill or knowledge being offered", 
        example = "Spring Boot Performance Tuning", 
        maxLength = 100
    )
    @NotBlank(message = "Title is required")
    @Size(max = 100, message = "Title must be under 100 characters")
    String title,

    @Schema(
        description = "A detailed description of what the learner will gain from this exchange", 
        example = "Deep dive into JVM metrics and profiling tools.", 
        maxLength = 1000
    )
    @Size(max = 1000, message = "Description must be under 1000 characters")
    String description
) {
}
