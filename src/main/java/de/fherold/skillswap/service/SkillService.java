package de.fherold.skillswap.service;

import de.fherold.skillswap.context.TenantContext;
import de.fherold.skillswap.dto.SkillRequestDTO;
import de.fherold.skillswap.dto.SkillResponseDTO;
import de.fherold.skillswap.dto.SwapTransactionResponseDTO;
import de.fherold.skillswap.exception.BusinessRuleException;
import de.fherold.skillswap.exception.ResourceNotFoundException;
import de.fherold.skillswap.model.Skill;
import de.fherold.skillswap.model.SwapTransaction;
import de.fherold.skillswap.model.User;
import de.fherold.skillswap.repository.SkillRepository;
import de.fherold.skillswap.repository.SwapTransactionRepository;
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
    private final SwapTransactionRepository swapTransactionRepository;

    public List<SkillResponseDTO> getAllSkills() {
        return skillRepository.findAll().stream()
            .map(this::mapToDTO)
            .toList();
    }

    public SkillResponseDTO getSkillById(Long id) {
        return skillRepository.findByIdAndTenantId(id, TenantContext.getTenantId())
            .map(this::mapToDTO)
            .orElseThrow(() -> new ResourceNotFoundException("Skill not found or access denied"));
    }

    @Transactional
    public SkillResponseDTO createSkill(SkillRequestDTO dto) {
        String currentTenant = TenantContext.getTenantId();
        
        User provider = userRepository.findByUsername(dto.getProviderUsername())
            .filter(u -> u.getTenantId().equals(currentTenant))
            .orElseThrow(() -> new BusinessRuleException("Invalid provider for this tenant", "INVALID_PROVIDER"));

        Skill skill = new Skill();
        skill.setTitle(dto.getTitle());
        skill.setDescription(dto.getDescription());
        skill.setProvider(provider);
        
        skill.setTenantId(currentTenant);

        return mapToDTO(skillRepository.save(skill));
    }

    @Transactional
    public SkillResponseDTO updateSkill(Long id, SkillRequestDTO dto) {
        // Fetch with ID + Tenant ensures you can't update another company's skill
        Skill skill = skillRepository.findByIdAndTenantId(id, TenantContext.getTenantId())
            .orElseThrow(() -> new ResourceNotFoundException("Skill not found or access denied"));

        skill.setTitle(dto.getTitle());
        skill.setDescription(dto.getDescription());
        
        return mapToDTO(skillRepository.save(skill));
    }

    @Transactional
    public void deleteSkill(Long id) {
        // Fetch with ID + Tenant ensures you can't delete another company's skill
        Skill skill = skillRepository.findByIdAndTenantId(id, TenantContext.getTenantId())
            .orElseThrow(() -> new ResourceNotFoundException("Skill not found or access denied"));

        skillRepository.delete(skill);
    }

    @Transactional
    public void performSwap(Long studentId, Long skillId) {
        String currentTenant = TenantContext.getTenantId();

        // 1. Fetch student and ensure they belong to the current tenant
        User student = userRepository.findById(studentId)
            .filter(u -> u.getTenantId().equals(currentTenant))
            .orElseThrow(() -> new ResourceNotFoundException("Student not found or access denied"));

        // 2. Fetch skill and ensure it belongs to the current tenant
        Skill skill = skillRepository.findByIdAndTenantId(skillId, currentTenant)
            .orElseThrow(() -> new ResourceNotFoundException("Skill not found or access denied"));

        User provider = skill.getProvider();

        if (student.getCredits() <= 0) {
            throw new BusinessRuleException("Student does not have enough credits", "INSUFFICIENT_CREDITS");
        }

        if (student.getId().equals(provider.getId())) {
            throw new BusinessRuleException("Student cannot swap with themselves", "SELF_SWAP_NOT_ALLOWED");
        }

        student.setCredits(student.getCredits() - 1);
        provider.setCredits(provider.getCredits() + 1);

        SwapTransaction swapTransaction = SwapTransaction.builder()
            .studentId(student.getId())
            .providerId(provider.getId())
            .skillId(skill.getId())
            .skillTitle(skill.getTitle())
            .creditAmount(1)
            .tenantId(currentTenant) // Added tenantId to transaction too!
            .build();

        swapTransactionRepository.save(swapTransaction);
    }

    // --- SEARCH & HISTORY ---

    public List<SkillResponseDTO> searchSkillsByTitle(String title) {
        if (title == null || title.isBlank()) {
            return getAllSkills();
        }
        // Aspect will automatically add "AND tenant_id = ..." to the query generated by Spring Data
        return skillRepository.findByTitleContainingIgnoreCase(title).stream()
            .map(this::mapToDTO)
            .toList();
    }

    public List<SwapTransactionResponseDTO> getSwapHistoryByStudent(Long studentId) {
        // Security check: Ensure student belongs to the logged in tenant
        User student = userRepository.findById(studentId)
                .filter(u -> u.getTenantId().equals(TenantContext.getTenantId()))
                .orElseThrow(() -> new ResourceNotFoundException("History access denied or student not found"));

        return swapTransactionRepository.findByStudentId(student.getId()).stream()
            .map(this::mapToTransactionDTO)
            .toList();
    }

    // --- MAPPERS ---

    private SkillResponseDTO mapToDTO(Skill skill) {
        return new SkillResponseDTO(
            skill.getId(),
            skill.getTitle(),
            skill.getDescription(),
            skill.getProvider() != null ? skill.getProvider().getUsername() : "Unknown Provider"
        );
    }

    private SwapTransactionResponseDTO mapToTransactionDTO(SwapTransaction transaction) {
        return new SwapTransactionResponseDTO(
            transaction.getId(),
            transaction.getStudentId(),
            transaction.getProviderId(),
            transaction.getSkillId(),
            transaction.getSkillTitle(),
            transaction.getCreditAmount(),
            transaction.getSwappedAt()
        );
    }
}
