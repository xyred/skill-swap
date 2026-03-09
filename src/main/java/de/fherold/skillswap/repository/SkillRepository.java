package de.fherold.skillswap.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import de.fherold.skillswap.model.Skill;

public interface SkillRepository extends JpaRepository<Skill, Long> {
    List<Skill> findByTitleContainingIgnoreCase(String title);

    Optional<Skill> findByIdAndTenantId(Long id, String tenantId);
}
