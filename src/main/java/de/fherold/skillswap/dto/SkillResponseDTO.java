package de.fherold.skillswap.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Data Transfer Object representing a skill available for swap")
public record SkillResponseDTO(

    @Schema(description = "Unique identifier of the skill", example = "1")
    Long id,

    @Schema(description = "The title of the skill being offered", example = "Spring Boot 4 Development")
    String title,

    @Schema(description = "A detailed description of what the user will learn",
        example = "Learn how to build reactive microservices using Spring Boot 4.")
    String description,

    @Schema(description = "The username of the provider offering the skill", example = "fherold")
    String providerName
) {
}
