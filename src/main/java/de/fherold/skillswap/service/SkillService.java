package de.fherold.skillswap.service;

import de.fherold.skillswap.dto.SkillResponseDTO;
import de.fherold.skillswap.exception.BusinessRuleException;
import de.fherold.skillswap.exception.ResourceNotFoundException;
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
@Transactional(readOnly = true)
public class SkillService {

    private final SkillRepository skillRepository;
    private final UserRepository userRepository;

    @Transactional
    public void performSwap(Long studentId, Long skillId) {
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with ID: " + studentId));

        Skill skill = skillRepository.findById(skillId)
                .orElseThrow(() -> new ResourceNotFoundException("Skill not found with ID: " + skillId));

        User provider = skill.getProvider();

        if (student.getCredits() <= 0) {
            throw new BusinessRuleException("Student does not have enough credits");
        }

        if (student.getId().equals(provider.getId())) {
            throw new BusinessRuleException("Student cannot swap with themselves");
        }

        student.setCredits(student.getCredits() - 1);
        provider.setCredits(provider.getCredits() + 1);

        userRepository.save(student);
        userRepository.save(provider);
    }

    public List<SkillResponseDTO> getAllSkills() {
        return skillRepository.findAll().stream()
                .map(this::mapToDTO)
                .toList();
    }

    public List<SkillResponseDTO> searchSkillsByTitle(String title) {
        if (title == null || title.isBlank()) {
            return getAllSkills();
        }

        return skillRepository.findByTitleContainingIgnoreCase(title).stream()
                .map(this::mapToDTO)
                .toList();
    }

    public SkillResponseDTO getSkillById(Long id) {
        return skillRepository.findById(id)
                .map(this::mapToDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Skill not found with ID: " + id));
    }

    private SkillResponseDTO mapToDTO(Skill skill) {
        return new SkillResponseDTO(
                skill.getId(),
                skill.getTitle(),
                skill.getDescription(),
                skill.getProvider() != null ? skill.getProvider().getUsername() : "Unknown Provider"
        );
    }
}
