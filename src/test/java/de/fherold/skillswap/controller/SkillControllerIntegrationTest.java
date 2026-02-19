package de.fherold.skillswap.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class SkillControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldReturnAllSkills() throws Exception {
        mockMvc.perform(get("/api/skills"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].title").value("Java Programming"));
    }

    @Test
    void shouldPerformSuccessfulSwap() throws Exception {
        mockMvc.perform(post("/api/skills/swap")
                        .param("studentId", "1")
                        .param("skillId", "100"))
                .andExpect(status().isOk())
                .andExpect(content().string("Swap successful!"));
    }

    @Test
    void shouldFailSwapIfInsufficientCredits() throws Exception {
        for (int i = 0; i < 5; i++) {
            mockMvc.perform(post("/api/skills/swap")
                            .param("studentId", "1")
                            .param("skillId", "100"))
                    .andExpect(status().isOk());
        }

        mockMvc.perform(post("/api/skills/swap")
                        .param("studentId", "1")
                        .param("skillId", "100"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Student does not have enough credits"));
    }

    @Test
    void shouldReturn400IfUserDoesNotExist() throws Exception {
        mockMvc.perform(post("/api/skills/swap")
                        .param("studentId", "999")
                        .param("skillId", "100"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldFilterSkillsByTitle() throws Exception {
        mockMvc.perform(get("/api/skills").param("search", "java"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].title").value("Java Programming"));

        mockMvc.perform(get("/api/skills").param("search", "python"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void shouldReturnCustomErrorWhenSkillNotFound() throws Exception {
        mockMvc.perform(get("/api/skills/999")) // ID 999 doesn't exist
                .andExpect(status().isNotFound()) // Verify it's 404
                .andExpect(jsonPath("$.errorCode").value("RESOURCE_NOT_FOUND"))
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.timestamp").exists());
    }
}
