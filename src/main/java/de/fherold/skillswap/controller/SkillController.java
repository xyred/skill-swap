package de.fherold.skillswap.controller;

import de.fherold.skillswap.dto.SkillResponseDTO;
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
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/skills")
@RequiredArgsConstructor
@Validated
@Tag(name = "Skill Management", description = "Endpoints for browsing skills and executing the credit-based exchange.")
public class SkillController {

    private final SkillService skillService;

    @Operation(summary = "Perform a skill swap", description = "Executes a credit exchange: 1 credit is moved from the student to the skill provider.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Swap completed successfully"),
        @ApiResponse(responseCode = "400", description = "Business rule violation (e.g., self-swap or insufficient credits)",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "404", description = "The specified student or skill does not exist",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/swap")
    public ResponseEntity<String> performSwap(
        @Parameter(description = "ID of the student receiving the skill", example = "1")
        @RequestParam @NotNull(message = "Student ID is required") Long studentId,

        @Parameter(description = "ID of the skill being learned", example = "42")
        @RequestParam @NotNull(message = "Skill ID is required") Long skillId) {

        skillService.performSwap(studentId, skillId);
        return ResponseEntity.ok("Swap successful!");
    }

    @Operation(summary = "Get skill by ID", description = "Returns detailed information about a specific skill.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Skill found and returned"),
        @ApiResponse(responseCode = "404", description = "Skill with the given ID does not exist",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<SkillResponseDTO> getSkillById(
        @Parameter(description = "The unique ID of the skill", example = "5")
        @PathVariable Long id) {
        return ResponseEntity.ok(skillService.getSkillById(id));
    }

    @Operation(summary = "List and search skills", description = "Retrieves all available skills. Use the 'search' parameter to filter by title.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = SkillResponseDTO.class))))
    })
    @GetMapping
    public ResponseEntity<List<SkillResponseDTO>> getAllSkills(
        @Parameter(description = "Optional keyword to filter skills by their title", example = "Spring")
        @RequestParam(required = false) String search) {

        return ResponseEntity.ok(skillService.searchSkillsByTitle(search));
    }
}
