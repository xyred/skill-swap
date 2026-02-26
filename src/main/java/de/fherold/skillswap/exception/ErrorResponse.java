package de.fherold.skillswap.exception;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Standard object for returning error details to the client")
public record ErrorResponse(
    @Schema(description = "The exact time the error occurred", example = "2026-02-25T14:30:00")
    LocalDateTime timestamp,

    @Schema(description = "A human-readable explanation of what went wrong", example = "Student does not have enough credits")
    String message,

    @Schema(description = "A specific internal error code for programmatic handling", example = "INSUFFICIENT_CREDITS")
    String errorCode
) {
}
