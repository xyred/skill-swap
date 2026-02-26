package de.fherold.skillswap.service;

import de.fherold.skillswap.model.Skill;
import de.fherold.skillswap.model.SwapTransaction;
import de.fherold.skillswap.model.User;
import de.fherold.skillswap.repository.SkillRepository;
import de.fherold.skillswap.repository.SwapTransactionRepository;
import de.fherold.skillswap.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SkillServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private SkillRepository skillRepository;

    @Mock
    private SwapTransactionRepository transactionRepository;

    @InjectMocks
    private SkillService skillService;

    @Test
    @DisplayName("Should transfer 1 credit from student to provider on successful swap")
    void shouldTransferCreditsOnSuccess() {
        User student = new User();
        student.setId(1L);
        student.setCredits(5);

        User provider = new User();
        provider.setId(2L);
        provider.setCredits(5);

        Skill skill = new Skill();
        skill.setId(100L);
        skill.setProvider(provider);

        when(userRepository.findById(1L)).thenReturn(Optional.of(student));
        when(skillRepository.findById(100L)).thenReturn(Optional.of(skill));

        skillService.performSwap(1L, 100L);

        assertEquals(4, student.getCredits(), "Student should have 1 credit less");
        assertEquals(6, provider.getCredits(), "Provider should have 1 credit more");
    }

    @Test
    @DisplayName("Should throw exception when user attempts to swap their own skill")
    void shouldThrowExceptionForSelfSwap() {
        User user = new User();
        user.setId(1L);

        Skill myOwnSkill = new Skill();
        myOwnSkill.setId(100L);
        myOwnSkill.setProvider(user);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(skillRepository.findById(100L)).thenReturn(Optional.of(myOwnSkill));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> skillService.performSwap(1L, 100L));

        assertEquals("Student cannot swap with themselves", exception.getMessage());

        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw exception when student has 0 credits")
    void shouldThrowExceptionWhenCreditsAreZero() {
        User brokeStudent = new User();
        brokeStudent.setId(1L);
        brokeStudent.setCredits(0);

        Skill skill = new Skill();
        skill.setId(100L);
        skill.setProvider(new User());

        when(userRepository.findById(1L)).thenReturn(Optional.of(brokeStudent));
        when(skillRepository.findById(100L)).thenReturn(Optional.of(skill));

        assertThrows(RuntimeException.class, () -> skillService.performSwap(1L, 100L));

        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should transfer credits and record a transaction on success")
    void shouldTransferCreditsAndRecordTransaction() {
        // 1. Arrange (Same as before)
        User student = new User();
        student.setId(1L);
        student.setCredits(5);

        User provider = new User();
        provider.setId(2L);
        provider.setCredits(5);
        provider.setUsername("ExpertDev");

        Skill skill = new Skill();
        skill.setId(100L);
        skill.setTitle("Java Mastery");
        skill.setProvider(provider);

        when(userRepository.findById(1L)).thenReturn(Optional.of(student));
        when(skillRepository.findById(100L)).thenReturn(Optional.of(skill));

        // 2. Act
        skillService.performSwap(1L, 100L);

        // 3. Assert (State checks)
        assertEquals(4, student.getCredits());
        assertEquals(6, provider.getCredits());

        // 4. Verification (The "Red" part)
        // We verify that save() was called with ANY SwapTransaction object
        // In a second, this will fail because the Service doesn't even have the repo yet!
        verify(transactionRepository).save(any(SwapTransaction.class));
    }
}
