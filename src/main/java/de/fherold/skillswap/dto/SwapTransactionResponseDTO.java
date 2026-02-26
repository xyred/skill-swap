package de.fherold.skillswap.dto;

import java.time.LocalDateTime;

/**
 * DTO for displaying a history of skill swaps.
 */

public record SwapTransactionResponseDTO(
    Long id,
    Long studentId,
    Long providerId,
    Long skillId,
    String skillTitle,
    Integer creditAmount,
    LocalDateTime swappedAt
) {
}
