package de.fherold.skillswap.repository;

import de.fherold.skillswap.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Transactional
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("Should save and find user by email")
    void shouldSaveAndFindUser() {
        User user = new User();
        user.setUsername("testuser");
        user.setEmail("testuser@skillswap.de");
        user.setPassword("securePassword");

        userRepository.save(user);
        Optional<User> foundUser = userRepository.findByEmail("testuser@skillswap.de");

        assertTrue(foundUser.isPresent(), "User should be found in the database");
        assertEquals("testuser", foundUser.get().getUsername(), "Username should match");
        assertEquals(5, foundUser.get().getCredits(), "Default credits should be 5");
    }

    @Test
    @DisplayName("Should throw exception when email is duplicate")
    void shouldThrowExceptionWhenEmailIsDuplicate() {
        User user1 = new User();
        user1.setUsername("user1");
        user1.setEmail("duplicate@test.com");
        user1.setPassword("pass1");
        userRepository.saveAndFlush(user1);

        User user2 = new User();
        user2.setUsername("user2");
        user2.setEmail("duplicate@test.com");
        user2.setPassword("pass2");

        assertThrows(org.springframework.dao.DataIntegrityViolationException.class, () -> {
            userRepository.saveAndFlush(user2);
        }, "Database should prevent duplicate emails");
    }
}
