package de.fherold.skillswap.repository;

import de.fherold.skillswap.model.Skill;
import de.fherold.skillswap.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@DataJpaTest
class SkillRepositoryTest {
    @Autowired
    private SkillRepository skillRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("Should find skills by provider")
    void shouldLinkSkillToUser() {
        User provider = new User();
        provider.setUsername("teacher_tom");
        provider.setEmail("tom@test.com");
        provider.setPassword("password");
        userRepository.save(provider);

        Skill skill = new Skill();
        skill.setTitle("Spring Boot Basics");
        skill.setDescription("Learn the fundamentals of Spring");
        skill.setProvider(provider);

        skillRepository.save(skill);

        List<Skill> allSkills = skillRepository.findAll();
        assertFalse(allSkills.isEmpty());
        assertEquals("teacher_tom", allSkills.getFirst().getProvider().getUsername());
    }
}
