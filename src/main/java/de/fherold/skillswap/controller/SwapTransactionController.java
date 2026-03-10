package de.fherold.skillswap.controller;

import de.fherold.skillswap.dto.SwapTransactionResponseDTO;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
@Tag(name = "Transaction Management", description = "Endpoints for viewing the credit-exchange history.")
public class SwapTransactionController {

    private final SkillService skillService;

    @Operation(summary = "Get swap history for a specific student", 
               description = "Returns a list of all skills a student has 'purchased'. Access is restricted to the student's own organization.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "History retrieved successfully",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = SwapTransactionResponseDTO.class)))),
        @ApiResponse(responseCode = "404", description = "Student not found or belongs to another tenant",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<SwapTransactionResponseDTO>> getStudentHistory(
            @Parameter(description = "ID of the student", example = "2")
            @PathVariable Long studentId) {
        return ResponseEntity.ok(skillService.getSwapHistoryByStudent(studentId));
    }
}
