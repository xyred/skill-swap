package de.fherold.skillswap.repository;

import de.fherold.skillswap.model.Skill;
import de.fherold.skillswap.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
        provider.setUsername("test_teacher");
        provider.setEmail("test_teacher@test.com");
        provider.setPassword("password");
        userRepository.save(provider);

        Skill skill = new Skill();
        skill.setTitle("Spring Boot Basics");
        skill.setDescription("Learn the fundamentals of Spring");
        skill.setProvider(provider);

        skillRepository.save(skill);

        List<Skill> foundSkills = skillRepository.findByTitleContainingIgnoreCase("Spring Boot Basics");

        assertEquals(1, foundSkills.size(), "Should find exactly the skill we just created");
        assertEquals("test_teacher", foundSkills.getFirst().getProvider().getUsername(), "Provider's username should match");
    }
}
