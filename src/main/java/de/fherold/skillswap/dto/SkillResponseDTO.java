package de.fherold.skillswap.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;

/**
 * Detailed representation of a skill, including its provider's identity.
 */
@Schema(description = "Detailed representation of a skill, including its provider's identity.")
public record SkillResponseDto(

        @Schema(description = "Unique identifier of the skill", example = "1", requiredMode = RequiredMode.REQUIRED) Long id,

        @Schema(description = "The title of the skill being offered", example = "Java 21 Performance Tuning", requiredMode = RequiredMode.REQUIRED) String title,

        @Schema(description = "A detailed description of the skill curriculum", example = "Optimizing JVM parameters and analyzing heap dumps.", requiredMode = RequiredMode.REQUIRED) String description,

        @Schema(description = "The username of the provider offering the skill", example = "fherold", requiredMode = RequiredMode.REQUIRED) String providerName) {
}
