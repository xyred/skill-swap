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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service handling skill operations and credit-based swaps.
 * Implements multi-tenant security with a Super Admin bypass.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SkillService {

    private final SkillRepository skillRepository;
    private final UserRepository userRepository;
    private final SwapTransactionRepository swapTransactionRepository;

    // --- HELPER METHODS ---

    /**
     * Centralized security check to fetch a skill.
     * Regular users are restricted to their tenant; ROLE_SUPER_ADMIN can access anything.
     */
    private Skill fetchSkillSecurely(Long id) {
        if (isSuperAdmin()) {
            return skillRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Skill not found (Global)"));
        }
        return skillRepository.findByIdAndTenantId(id, TenantContext.getTenantId())
            .orElseThrow(() -> new ResourceNotFoundException("Skill not found or access denied"));
    }

    /**
     * Checks if the currently authenticated user is a Super Admin.
     */
    private boolean isSuperAdmin() {
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_SUPER_ADMIN"));
    }

    // --- SKILL CRUD ---

    public List<SkillResponseDTO> getAllSkills() {
        // Aspect automatically filters this for regular users
        // and skips filtering for Super Admins.
        return skillRepository.findAll().stream()
            .map(this::mapToDTO)
            .toList();
    }

    public SkillResponseDTO getSkillById(Long id) {
        return mapToDTO(fetchSkillSecurely(id));
    }

    @Transactional
    public SkillResponseDTO createSkill(SkillRequestDTO dto) {
        String currentTenant = TenantContext.getTenantId();
        
        // Find provider and ensure they belong to the same tenant as the creator
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
        Skill skill = fetchSkillSecurely(id);
        skill.setTitle(dto.getTitle());
        skill.setDescription(dto.getDescription());
        return mapToDTO(skillRepository.save(skill));
    }

    @Transactional
    public void deleteSkill(Long id) {
        Skill skill = fetchSkillSecurely(id);
        skillRepository.delete(skill);
    }

    // --- SEARCH & HISTORY ---

    public List<SkillResponseDTO> searchSkillsByTitle(String title) {
        if (title == null || title.isBlank()) {
            return getAllSkills();
        }
        return skillRepository.findByTitleContainingIgnoreCase(title).stream()
            .map(this::mapToDTO)
            .toList();
    }

    public List<SwapTransactionResponseDTO> getSwapHistoryByStudent(Long studentId) {
        // Security check: Super Admin sees all history; others only their tenant's.
        User student = userRepository.findById(studentId)
                .filter(u -> isSuperAdmin() || u.getTenantId().equals(TenantContext.getTenantId()))
                .orElseThrow(() -> new ResourceNotFoundException("Student history not found or access denied"));

        return swapTransactionRepository.findByStudentId(student.getId()).stream()
            .map(this::mapToTransactionDTO)
            .toList();
    }

    // --- THE SWAP LOGIC ---

    @Transactional
    public void performSwap(Long studentId, Long skillId) {
        String currentTenant = TenantContext.getTenantId();

        // Security: Swaps must stay within a single tenant to maintain credit economy.
        User student = userRepository.findById(studentId)
            .filter(u -> u.getTenantId().equals(currentTenant))
            .orElseThrow(() -> new ResourceNotFoundException("Student not found or access denied"));

        Skill skill = skillRepository.findByIdAndTenantId(skillId, currentTenant)
            .orElseThrow(() -> new ResourceNotFoundException("Skill not found or access denied"));

        User provider = skill.getProvider();

        if (student.getCredits() <= 0) {
            throw new BusinessRuleException("Student does not have enough credits", "INSUFFICIENT_CREDITS");
        }

        if (student.getId().equals(provider.getId())) {
            throw new BusinessRuleException("Student cannot swap with themselves", "SELF_SWAP_NOT_ALLOWED");
        }

        // Atomic Credit Update
        student.setCredits(student.getCredits() - 1);
        provider.setCredits(provider.getCredits() + 1);

        // Audit Trail
        SwapTransaction swapTransaction = SwapTransaction.builder()
            .studentId(student.getId())
            .providerId(provider.getId())
            .skillId(skill.getId())
            .skillTitle(skill.getTitle())
            .creditAmount(1)
            .tenantId(currentTenant)
            .build();

        swapTransactionRepository.save(swapTransaction);
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
