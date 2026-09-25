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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class EnrollmentTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void enrolledStudentsAreListed() throws Exception {
        long java = postBootcamp("Java");
        long ana = postStudent("Ana");
        postStudent("Bruno");

        mockMvc.perform(post("/bootcamps/{id}/students/{studentId}", java, ana))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/bootcamps/{id}/students", java))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("Ana"));
    }

    @Test
    void studentCanJoinSeveralBootcamps() throws Exception {
        long java = postBootcamp("Java");
        long python = postBootcamp("Python");
        long ana = postStudent("Ana");

        mockMvc.perform(post("/bootcamps/{id}/students/{studentId}", java, ana))
                .andExpect(status().isNoContent());
        mockMvc.perform(post("/bootcamps/{id}/students/{studentId}", python, ana))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/bootcamps/{id}/students", python))
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void enrollingTwiceReturns409() throws Exception {
        long java = postBootcamp("Java");
        long ana = postStudent("Ana");
        mockMvc.perform(post("/bootcamps/{id}/students/{studentId}", java, ana));

        mockMvc.perform(post("/bootcamps/{id}/students/{studentId}", java, ana))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.detail").value("O aluno %d já está matriculado no bootcamp %d".formatted(ana, java)));
    }

    @Test
    void enrollingUnknownStudentOrBootcampReturns404() throws Exception {
        long java = postBootcamp("Java");
        long ana = postStudent("Ana");

        mockMvc.perform(post("/bootcamps/{id}/students/{studentId}", java, 99))
                .andExpect(status().isNotFound());
        mockMvc.perform(post("/bootcamps/{id}/students/{studentId}", 99, ana))
                .andExpect(status().isNotFound());
    }

    @Test
    void unenrollRemovesOnlyTheEnrollment() throws Exception {
        long java = postBootcamp("Java");
        long ana = postStudent("Ana");
        mockMvc.perform(post("/bootcamps/{id}/students/{studentId}", java, ana));

        mockMvc.perform(delete("/bootcamps/{id}/students/{studentId}", java, ana))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/bootcamps/{id}/students", java))
                .andExpect(jsonPath("$.length()").value(0));
        mockMvc.perform(get("/students/{id}", ana))
                .andExpect(status().isOk());
    }

    @Test
    void unenrollStudentNotEnrolledReturns404() throws Exception {
        long java = postBootcamp("Java");
        long ana = postStudent("Ana");

        mockMvc.perform(delete("/bootcamps/{id}/students/{studentId}", java, ana))
                .andExpect(status().isNotFound());
    }

    @Test
    void bootcampWithEnrolledStudentCannotBeDeleted() throws Exception {
        long java = postBootcamp("Java");
        long ana = postStudent("Ana");
        mockMvc.perform(post("/bootcamps/{id}/students/{studentId}", java, ana));

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

    private long postStudent(String name) throws Exception {
        return idOf(mockMvc.perform(post("/students").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"%s\"}".formatted(name)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString());
    }

    private long idOf(String json) {
        return ((Number) JsonPath.read(json, "$.id")).longValue();
    }

}
