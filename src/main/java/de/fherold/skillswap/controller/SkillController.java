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

        skillService.performSwap(studentId, skillId);
        return ResponseEntity.ok("Swap successful!");
    }

    @GetMapping("/{id}")
    public ResponseEntity<SkillResponseDTO> getSkillById(@PathVariable Long id) {
        return ResponseEntity.ok(skillService.getSkillById(id));
    }

    @Operation(summary = "List all skills", description = "Returns all skills, or filters them by title if a search term is provided.")
    @GetMapping
    public ResponseEntity<List<SkillResponseDTO>> getAllSkills(@Parameter(description = "Optional search term to filter skills by title")
                                                               @RequestParam(required = false) String search) {

        List<SkillResponseDTO> skills = (search != null && !search.isBlank())
                ? skillService.searchSkillsByTitle(search)
                : skillService.getAllSkills();

        return ResponseEntity.ok(skills);
    }
}
