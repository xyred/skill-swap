package de.fherold.skillswap.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

/**
 * DTO for displaying a history of skill swaps.
 */
@Schema(description = "Audit log entry representing a successful credit exchange between two users.")
public record SwapTransactionResponseDto(

        @Schema(description = "The unique ID of the transaction record", example = "1001") Long id,

        @Schema(description = "ID of the student who spent a credit", example = "10") Long studentId,

        @Schema(description = "ID of the provider who earned a credit", example = "25") Long providerId,

        @Schema(description = "ID of the skill that was exchanged", example = "42") Long skillId,

        @Schema(description = "Snapshot of the skill title at the time of the swap", example = "Java 21 Performance Tuning") String skillTitle,

        @Schema(description = "Number of credits exchanged (usually 1)", example = "1") Integer creditAmount,

        @Schema(description = "The exact timestamp of the transaction", example = "2026-03-17T11:45:00") LocalDateTime swappedAt) {
}
