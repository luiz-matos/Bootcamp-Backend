package com.bootcamp.bootcampbackend.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;

class EnrollmentTest extends ApiTest {

    @Test
    void enrolledStudentsAreListed() throws Exception {
        long java = postBootcamp("Java");
        long ana = postStudent("Ana");
        postStudent("Bruno");

        mockMvc.perform(post("/bootcamps/{id}/students/{studentId}", java, ana)).andExpect(status().isNoContent());

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

        mockMvc.perform(post("/bootcamps/{id}/students/{studentId}", java, ana)).andExpect(status().isNoContent());
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
                .andExpect(jsonPath("$.detail")
                        .value("O aluno %d já está matriculado no bootcamp %d".formatted(ana, java)));
    }

    @Test
    void enrollingUnknownStudentOrBootcampReturns404() throws Exception {
        long java = postBootcamp("Java");
        long ana = postStudent("Ana");

        mockMvc.perform(post("/bootcamps/{id}/students/{studentId}", java, 99)).andExpect(status().isNotFound());
        mockMvc.perform(post("/bootcamps/{id}/students/{studentId}", 99, ana)).andExpect(status().isNotFound());
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
        mockMvc.perform(get("/students/{id}", ana)).andExpect(status().isOk());
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

        mockMvc.perform(delete("/bootcamps/{id}", java)).andExpect(status().isConflict());
    }
}
