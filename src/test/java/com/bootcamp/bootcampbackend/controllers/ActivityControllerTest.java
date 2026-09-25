package com.bootcamp.bootcampbackend.controllers;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ActivityControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void postCreatesTheActivityInTheBootcamp() throws Exception {
        long java = postBootcamp("Java");

        mockMvc.perform(post("/bootcamps/{id}/activities", java).contentType(MediaType.APPLICATION_JSON).content("""
                        {"title": "Mentoria de Spring", "description": "Tira-dúvidas", "dateOfMentoring": "2024-01-15"}
                        """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.title").value("Mentoria de Spring"))
                .andExpect(jsonPath("$.dateOfMentoring").value("2024-01-15"))
                .andExpect(jsonPath("$.bootcamp").doesNotExist());
    }

    @Test
    void postWithoutRequiredFieldsReturns400() throws Exception {
        long java = postBootcamp("Java");

        mockMvc.perform(post("/bootcamps/{id}/activities", java).contentType(MediaType.APPLICATION_JSON).content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.title").value("O título é obrigatório"))
                .andExpect(jsonPath("$.errors.dateOfMentoring").value("A data da mentoria é obrigatória"));
    }

    @Test
    void postInUnknownBootcampReturns404() throws Exception {
        mockMvc.perform(post("/bootcamps/{id}/activities", 99).contentType(MediaType.APPLICATION_JSON).content("""
                        {"title": "Mentoria", "dateOfMentoring": "2024-01-15"}
                        """))
                .andExpect(status().isNotFound());
    }

    @Test
    void listReturnsOnlyTheActivitiesOfTheBootcamp() throws Exception {
        long java = postBootcamp("Java");
        long python = postBootcamp("Python");
        postActivity(java, "Mentoria de Spring");
        postActivity(java, "Mentoria de JPA");
        postActivity(python, "Mentoria de Django");

        mockMvc.perform(get("/bootcamps/{id}/activities", java))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));

        mockMvc.perform(get("/bootcamps/{id}/activities", 99))
                .andExpect(status().isNotFound());
    }

    @Test
    void activityIsFoundOnlyThroughItsBootcamp() throws Exception {
        long java = postBootcamp("Java");
        long python = postBootcamp("Python");
        long activity = postActivity(java, "Mentoria de Spring");

        mockMvc.perform(get("/bootcamps/{id}/activities/{activityId}", java, activity))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Mentoria de Spring"));

        mockMvc.perform(get("/bootcamps/{id}/activities/{activityId}", python, activity))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.detail")
                        .value("Atividade %d não encontrada no bootcamp %d".formatted(activity, python)));
    }

    @Test
    void putUpdatesTheActivity() throws Exception {
        long java = postBootcamp("Java");
        long activity = postActivity(java, "Mentoria de Spring");

        mockMvc.perform(put("/bootcamps/{id}/activities/{activityId}", java, activity)
                        .contentType(MediaType.APPLICATION_JSON).content("""
                                {"title": "Mentoria de Spring Boot 4", "dateOfMentoring": "2024-01-22"}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Mentoria de Spring Boot 4"))
                .andExpect(jsonPath("$.dateOfMentoring").value("2024-01-22"));
    }

    @Test
    void deleteRemovesTheActivity() throws Exception {
        long java = postBootcamp("Java");
        long activity = postActivity(java, "Mentoria de Spring");

        mockMvc.perform(delete("/bootcamps/{id}/activities/{activityId}", java, activity))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/bootcamps/{id}/activities", java))
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void bootcampWithActivitiesCannotBeDeleted() throws Exception {
        long java = postBootcamp("Java");
        postActivity(java, "Mentoria de Spring");

        mockMvc.perform(delete("/bootcamps/{id}", java))
                .andExpect(status().isConflict());
    }

    private long postBootcamp(String name) throws Exception {
        return idOf(mockMvc.perform(post("/bootcamps").contentType(MediaType.APPLICATION_JSON).content("""
                        {"name": "%s", "creditHours": 40, "startDate": "2024-01-08", "endDate": "2024-03-01"}
                        """.formatted(name)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString());
    }

    private long postActivity(long bootcampId, String title) throws Exception {
        return idOf(mockMvc.perform(post("/bootcamps/{id}/activities", bootcampId)
                        .contentType(MediaType.APPLICATION_JSON).content("""
                                {"title": "%s", "dateOfMentoring": "2024-01-15"}
                                """.formatted(title)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString());
    }

    private long idOf(String json) {
        return ((Number) JsonPath.read(json, "$.id")).longValue();
    }

}
