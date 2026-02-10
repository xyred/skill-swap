package de.fherold.skillswap.controller;

import de.fherold.skillswap.dto.SkillResponseDTO;
import de.fherold.skillswap.service.SkillService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/skills")
@RequiredArgsConstructor
@Tag(name = "SkillController", description = "Endpoints for managing skills and performing swaps")
public class SkillController {

    private final SkillService skillService;

    @Operation(summary = "Perform a skill swap", description = "Deducts 1 credit from the student and adds it to the provider.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Swap completed successfully"),
            @ApiResponse(responseCode = "400", description = "Insufficient credits or invalid swap attempt")
    })
    @PostMapping("/swap")
    public ResponseEntity<String> performSwap(
            @Parameter(description = "ID of the student receiving the skill") @RequestParam Long studentId,
            @Parameter(description = "ID of the skill being learned") @RequestParam Long skillId) {

        try {
            skillService.performSwap(studentId, skillId);
            return ResponseEntity.ok("Swap successful!");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "List all available skills", description = "Returns a list of skills with provider names (passwords hidden).")
    @GetMapping
    public ResponseEntity<List<SkillResponseDTO>> getAllSkills() {
        return ResponseEntity.ok(skillService.getAllSkills());
    }
}
