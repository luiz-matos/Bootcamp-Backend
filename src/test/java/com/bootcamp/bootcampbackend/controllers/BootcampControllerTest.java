package com.bootcamp.bootcampbackend.controllers;

import com.bootcamp.bootcampbackend.repositories.BootcampRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class BootcampControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BootcampRepository bootcampRepository;

    @Test
    void createdBootcampIsSaved() throws Exception {
        postBootcampJson("""
                {"id": 1, "name": "Java", "description": "Trilha de back-end", "creditHours": 40,
                 "startDate": "2024-01-08", "endDate": "2024-03-01"}
                """);

        assertEquals(1, bootcampRepository.count());
    }

    private void postBootcampJson(String json) throws Exception {
        mockMvc.perform(post("/bootcamp").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isCreated());
    }

}
