package de.fherold.skillswap.repository;

import de.fherold.skillswap.model.Skill;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SkillRepository extends JpaRepository<Skill, Long> {
}