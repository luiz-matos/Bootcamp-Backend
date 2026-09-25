package com.bootcamp.bootcampbackend.controllers;

import com.bootcamp.bootcampbackend.entities.Bootcamp;
import com.bootcamp.bootcampbackend.repositories.BootcampRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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

    @Test
    void listReturnsOnlyBootcampData() throws Exception {
        postBootcampJson("""
                {"id": 1, "name": "Java", "description": "Trilha de back-end", "creditHours": 40,
                 "startDate": "2024-01-08", "endDate": "2024-03-01"}
                """);

        mockMvc.perform(get("/bootcamps"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("Java"))
                .andExpect(jsonPath("$[0].creditHours").value(40))
                .andExpect(jsonPath("$[0].startDate").value("2024-01-08"))
                .andExpect(jsonPath("$[0].students").doesNotExist())
                .andExpect(jsonPath("$[0].activities").doesNotExist());
    }

    @Test
    void eachPostCreatesANewBootcamp() throws Exception {
        postBootcamp("Java");
        postBootcamp("Python");

        assertEquals(2, bootcampRepository.count());
    }

    @Test
    void postIgnoresIdSentByClient() throws Exception {
        postBootcamp("Java");
        var java = bootcampRepository.findAll().getFirst();

        postBootcampJson("""
                {"id": %d, "name": "Python", "creditHours": 40,
                 "startDate": "2024-01-08", "endDate": "2024-03-01"}
                """.formatted(java.getId()));

        var names = bootcampRepository.findAll().stream().map(Bootcamp::getName).sorted().toList();
        assertEquals(List.of("Java", "Python"), names);
    }

    private void postBootcamp(String name) throws Exception {
        postBootcampJson("""
                {"name": "%s", "description": "Trilha de back-end", "creditHours": 40,
                 "startDate": "2024-01-08", "endDate": "2024-03-01"}
                """.formatted(name));
    }

    private void postBootcampJson(String json) throws Exception {
        mockMvc.perform(post("/bootcamps").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isCreated());
    }

}
