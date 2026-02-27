package de.fherold.skillswap.controller;

import de.fherold.skillswap.dto.SwapTransactionResponseDTO;
import de.fherold.skillswap.service.SkillService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/transactions")
@RequiredArgsConstructor
@Tag(name = "Transactions", description = "Endpoints for viewing swap history")
public class SwapTransactionController {

    private final SkillService skillService;

    @GetMapping("/student/{studentId}")
    @Operation(summary = "Get swap history for a specific student",
        description = "Returns a list of all skills a student has 'purchased' using credits.")
    public List<SwapTransactionResponseDTO> getStudentHistory(@PathVariable Long studentId) {
        return skillService.getSwapHistoryByStudent(studentId);
    }
}
