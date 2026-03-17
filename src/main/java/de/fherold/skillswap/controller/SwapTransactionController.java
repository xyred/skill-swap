package de.fherold.skillswap.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.fherold.skillswap.dto.SwapTransactionResponseDto;
import de.fherold.skillswap.exception.ErrorResponse;
import de.fherold.skillswap.service.SkillService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
@Tag(name = "Transaction Management", description = "Endpoints for viewing the credit-exchange history.")
public class SwapTransactionController {

    private final SkillService skillService;

    @Operation(
        summary = "Get swap history for a specific student", 
        description = "Returns a list of all skills a student has acquired. **Note:** Access is restricted by organization. Unauthorized or unauthenticated requests will receive a 403."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "History retrieved successfully. Returns an empty list if no transactions exist.",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = SwapTransactionResponseDto.class)))
        ),
        @ApiResponse(
            responseCode = "403", 
            description = "Forbidden - Missing/Invalid JWT or insufficient permissions",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        ),
        @ApiResponse(
            responseCode = "404", 
            description = "Student not found or access denied due to tenant isolation",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<SwapTransactionResponseDto>> getStudentHistory(
            @Parameter(description = "Database ID of the student", example = "2")
            @PathVariable Long studentId) {
        return ResponseEntity.ok(skillService.getSwapHistoryByStudent(studentId));
    }
}
