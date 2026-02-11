package de.fherold.skillswap.repository;

import de.fherold.skillswap.model.Skill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SkillRepository extends JpaRepository<Skill, Long> {
    List<Skill> findByTitleContainingIgnoreCase(String title);
}
