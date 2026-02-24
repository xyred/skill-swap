package de.fherold.skillswap.dto;

public record SkillResponseDTO(
        Long id,
        String title,
        String description,
        String providerName
) {
}
