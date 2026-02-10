package de.fherold.skillswap.controller;

import de.fherold.skillswap.service.SkillService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/skills")
@RequiredArgsConstructor
public class SkillController {

    private final SkillService skillService;

    @PostMapping("/swap")
    public ResponseEntity<String> performSwap(
            @RequestParam Long studentId,
            @RequestParam Long skillId) {

        try {
            skillService.performSwap(studentId, skillId);
            return ResponseEntity.ok("Swap successful!");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
