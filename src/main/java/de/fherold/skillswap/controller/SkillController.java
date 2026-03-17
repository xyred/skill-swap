package de.fherold.skillswap.controller;

import de.fherold.skillswap.dto.SkillRequestDto;
import de.fherold.skillswap.dto.SkillResponseDto;
import de.fherold.skillswap.exception.ErrorResponse;
import de.fherold.skillswap.service.SkillService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/skills")
@RequiredArgsConstructor
@Validated
@Tag(name = "Skill Management", description = "CRUD-Endpoints for skills and executing the credit-based exchange.")
@SecurityRequirement(name = "bearerAuth")
public class SkillController {

    private final SkillService skillService;

    @Operation(summary = "Perform a skill swap", description = "Executes an atomic credit exchange. 1 credit is deducted from the student and granted to the provider. "
            +
            "Constraint: Both participants must belong to the same tenant. Operation is transactional.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Swap successful!", content = @Content(mediaType = "application/json", schema = @Schema(example = "{\"message\": \"Swap successful\"}"))),
            @ApiResponse(responseCode = "400", description = "Business rule violation", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class), examples = {
                    @ExampleObject(name = "Insufficient Credits", value = """
                            {
                              "timestamp": "2026-03-16T14:00:00",
                              "message": "Student has 0 credits remaining.",
                              "errorCode": "INSUFFICIENT_CREDITS"
                            }
                            """),
                    @ExampleObject(name = "Self-Swap", value = """
                            {
                              "timestamp": "2026-03-16T14:00:00",
                              "message": "Users cannot purchase their own skills.",
                              "errorCode": "INVALID_TRANSACTION"
                            }
                            """)
            })),
            @ApiResponse(responseCode = "403", description = "Tenant Mismatch", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Entity not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping(value = "/swap", produces = "application/json")
    public ResponseEntity<String> performSwap(
            @Parameter(description = "Database ID of the student learner", example = "101") @RequestParam @NotNull(message = "Student ID is required") Long studentId,
            @Parameter(description = "Database ID of the skill being acquired", example = "505") @RequestParam @NotNull(message = "Skill ID is required") Long skillId) {

        skillService.performSwap(studentId, skillId);
        return ResponseEntity.ok("Swap successful!");
    }

    @Operation(summary = "Create a new skill", description = "Adds a skill to the current tenant's catalog. The tenant ID is automatically assigned from the authenticated user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Skill created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping
    public ResponseEntity<SkillResponseDto> createSkill(@Valid @RequestBody SkillRequestDto skillRequestDto) {
        return new ResponseEntity<>(skillService.createSkill(skillRequestDto), HttpStatus.CREATED);
    }

    @Operation(summary = "Update an existing skill", description = "Modifies skill details. Note: Users can only update skills belonging to their own organization.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Skill updated successfully"),
            @ApiResponse(responseCode = "404", description = "Skill not found or belongs to another tenant", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<SkillResponseDto> updateSkill(
            @Parameter(description = "ID of the skill to update", example = "5") @PathVariable Long id,
            @Valid @RequestBody SkillRequestDto skillRequestDto) {
        return ResponseEntity.ok(skillService.updateSkill(id, skillRequestDto));
    }

    @Operation(summary = "Delete a skill", description = "Removes a skill from the catalog. **Security Constraint:** A user can only delete skills that belong to their own organization (tenant). "
            +
            "Attempting to delete a skill from another tenant will result in a 404 to prevent ID enumeration.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Skill successfully deleted. No content returned."),
            @ApiResponse(responseCode = "404", description = "Skill not found or access denied due to tenant isolation", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject(value = """
                    {
                      "timestamp": "2026-03-17T11:15:00",
                      "message": "Skill with ID 5 not found in your organization.",
                      "errorCode": "RESOURCE_NOT_FOUND"
                    }
                    """)))
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSkill(
            @Parameter(description = "The unique ID of the skill to be deleted", example = "5") @PathVariable Long id) {
        skillService.deleteSkill(id);
    }

    @Operation(summary = "Get skill by ID", description = "Returns detailed information about a specific skill. Access is restricted to the owner's tenant.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Skill found and returned"),
            @ApiResponse(responseCode = "404", description = "Skill with the given ID does not exist in your organization", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<SkillResponseDto> getSkillById(
            @Parameter(description = "The unique ID of the skill", example = "5") @PathVariable Long id) {
        return ResponseEntity.ok(skillService.getSkillById(id));
    }

    @Operation(summary = "List and search skills", description = "Retrieves skills available within the user's organization.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful retrieval", content = @Content(array = @ArraySchema(schema = @Schema(implementation = SkillResponseDto.class))))
    })
    @GetMapping
    public ResponseEntity<List<SkillResponseDto>> getAllSkills(
            @Parameter(description = "Optional keyword to filter skills by their title") @RequestParam(required = false) String search) {

        return ResponseEntity.ok(skillService.searchSkillsByTitle(search));
    }
}
