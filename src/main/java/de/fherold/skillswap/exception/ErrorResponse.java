package de.fherold.skillswap.exception;

import java.time.LocalDateTime;

public record ErrorResponse(
        LocalDateTime timestamp,
        String message,
        String errorCode
) {
}
