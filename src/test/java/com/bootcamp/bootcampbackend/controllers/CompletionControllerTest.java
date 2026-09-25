package com.bootcamp.bootcampbackend.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

class CompletionControllerTest extends ApiTest {

    private long java;
    private long spring;
    private long jpa;
    private long ana;

    @BeforeEach
    void setUp() throws Exception {
        java = postBootcamp("Java");
        spring = postActivity(java, "Mentoria de Spring");
        jpa = postActivity(java, "Mentoria de JPA");
        ana = postStudent("Ana");
        mockMvc.perform(post("/bootcamps/{id}/students/{studentId}", java, ana)).andExpect(status().isNoContent());
    }

    @Test
    void activityShowsItsXp() throws Exception {
        mockMvc.perform(get("/bootcamps/{id}/activities/{activityId}", java, spring))
                .andExpect(jsonPath("$.xp").value(35.0));
    }

    @Test
    void newStudentHasNoXp() throws Exception {
        mockMvc.perform(get("/students/{id}", ana)).andExpect(jsonPath("$.xp").value(0.0));
    }

    @Test
    void xpIsTheSumOfTheCompletedActivities() throws Exception {
        postActivity(java, "Mentoria de testes");

        complete(ana, spring).andExpect(status().isNoContent());
        complete(ana, jpa).andExpect(status().isNoContent());

        mockMvc.perform(get("/students/{id}", ana))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.xp").value(70.0));

        mockMvc.perform(get("/students")).andExpect(jsonPath("$[0].xp").value(70.0));

        mockMvc.perform(get("/students/{id}/completed-activities", ana))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void xpSentByClientIsIgnored() throws Exception {
        String response = mockMvc.perform(post("/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {"name": "Bruno", "xp": 999}
                        """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.xp").value(0.0))
                .andReturn()
                .getResponse()
                .getContentAsString();

        mockMvc.perform(get("/students/{id}", idOf(response)))
                .andExpect(jsonPath("$.xp").value(0.0));
    }

    @Test
    void bootcampShowsItsXp() throws Exception {
        mockMvc.perform(get("/bootcamps/{id}", java)).andExpect(jsonPath("$.xp").value(600.0));
    }

    @Test
    void finishingAllActivitiesAddsTheBootcampXp() throws Exception {
        complete(ana, spring);

        mockMvc.perform(get("/students/{id}", ana)).andExpect(jsonPath("$.xp").value(35.0));
        mockMvc.perform(get("/students/{id}/completed-bootcamps", ana))
                .andExpect(jsonPath("$.length()").value(0));

        complete(ana, jpa);

        mockMvc.perform(get("/students/{id}", ana)).andExpect(jsonPath("$.xp").value(670.0));
        mockMvc.perform(get("/students/{id}/completed-bootcamps", ana))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("Java"));
    }

    @Test
    void bootcampXpStaysWhenANewActivityIsAdded() throws Exception {
        complete(ana, spring);
        complete(ana, jpa);

        long testes = postActivity(java, "Mentoria de testes");

        mockMvc.perform(get("/students/{id}", ana)).andExpect(jsonPath("$.xp").value(670.0));

        complete(ana, testes);

        mockMvc.perform(get("/students/{id}", ana)).andExpect(jsonPath("$.xp").value(705.0));
        mockMvc.perform(get("/students/{id}/completed-bootcamps", ana))
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void bootcampXpFollowsTheCreditHours() throws Exception {
        long python = idOf(mockMvc.perform(post("/bootcamps")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {"name": "Python", "creditHours": 10, "startDate": "2024-01-08", "endDate": "2024-03-01"}
                        """))
                .andReturn()
                .getResponse()
                .getContentAsString());
        long django = postActivity(python, "Mentoria de Django");
        mockMvc.perform(post("/bootcamps/{id}/students/{studentId}", python, ana));

        complete(ana, django);

        mockMvc.perform(get("/students/{id}", ana)).andExpect(jsonPath("$.xp").value(185.0));
    }

    @Test
    void completingTwiceReturns409() throws Exception {
        complete(ana, spring);

        complete(ana, spring)
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.detail").value("O aluno %d já concluiu a atividade %d".formatted(ana, spring)));

        mockMvc.perform(get("/students/{id}", ana)).andExpect(jsonPath("$.xp").value(35.0));
    }

    @Test
    void studentNotEnrolledCannotCompleteTheActivity() throws Exception {
        long bruno = postStudent("Bruno");

        complete(bruno, spring)
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.detail")
                        .value("O aluno %d não está matriculado no bootcamp da atividade %d".formatted(bruno, spring)));
    }

    @Test
    void completingUnknownActivityReturns404() throws Exception {
        complete(ana, 99)
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.detail").value("Atividade 99 não encontrada"));
    }

    @Test
    void completedActivityCannotBeDeleted() throws Exception {
        complete(ana, spring);

        mockMvc.perform(delete("/bootcamps/{id}/activities/{activityId}", java, spring))
                .andExpect(status().isConflict());
    }

    @Test
    void studentWithCompletedActivitiesCanBeDeleted() throws Exception {
        complete(ana, spring);

        mockMvc.perform(delete("/students/{id}", ana)).andExpect(status().isNoContent());

        mockMvc.perform(delete("/bootcamps/{id}/activities/{activityId}", java, spring))
                .andExpect(status().isNoContent());
    }

    private ResultActions complete(long student, long activity) throws Exception {
        return mockMvc.perform(post("/students/{id}/completed-activities/{activityId}", student, activity));
    }
}
