package de.fherold.skillswap.service;

import de.fherold.skillswap.dto.SkillResponseDTO;
import de.fherold.skillswap.model.Skill;
import de.fherold.skillswap.model.User;
import de.fherold.skillswap.repository.SkillRepository;
import de.fherold.skillswap.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service class responsible for handling skill-related operations, including performing skill swaps between users.
 * It ensures that the business logic for swapping skills is correctly implemented, such as checking user credits
 * and preventing users from swapping with themselves.
 */

@Service
@RequiredArgsConstructor
public class SkillService {

    private final SkillRepository skillRepository;
    private final UserRepository userRepository;

    @Transactional
    public void performSwap(Long studentId, Long skillId) {
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found"));

        Skill skill = skillRepository.findById(skillId)
                .orElseThrow(() -> new IllegalArgumentException("Skill not found"));

        User provider = skill.getProvider();

        if (student.getCredits() <= 0) {
            throw new IllegalStateException("Student does not have enough credits");
        }

        if (student.getId().equals(provider.getId())) {
            throw new IllegalStateException("Student cannot swap with themselves");
        }

        student.setCredits(student.getCredits() - 1);
        provider.setCredits(provider.getCredits() + 1);

        userRepository.save(student);
        userRepository.save(provider);
    }

    public List<SkillResponseDTO> getAllSkills() {
        return skillRepository.findAll().stream()
                .map(skill -> new SkillResponseDTO(
                        skill.getId(),
                        skill.getTitle(),
                        skill.getDescription(),
                        skill.getProvider().getUsername()
                ))
                .toList();
    }
}
