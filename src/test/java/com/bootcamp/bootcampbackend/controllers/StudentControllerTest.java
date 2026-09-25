package com.bootcamp.bootcampbackend.controllers;

import com.bootcamp.bootcampbackend.entities.Bootcamp;
import com.bootcamp.bootcampbackend.repositories.BootcampRepository;
import com.bootcamp.bootcampbackend.repositories.StudentRepository;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private BootcampRepository bootcampRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void postCreatesTheStudent() throws Exception {
        mockMvc.perform(post("/students").contentType(MediaType.APPLICATION_JSON).content("""
                        {"id": 50, "name": "Ana"}
                        """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").value("Ana"));

        assertEquals(1, studentRepository.count());
    }

    @Test
    void postWithoutNameReturns400() throws Exception {
        mockMvc.perform(post("/students").contentType(MediaType.APPLICATION_JSON).content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.name").value("O nome é obrigatório"));
    }

    @Test
    void getListsAndFindsStudents() throws Exception {
        postStudent("Ana");
        long id = postStudent("Bruno");

        mockMvc.perform(get("/students"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));

        mockMvc.perform(get("/students/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Bruno"));
    }

    @Test
    void getByUnknownIdReturns404() throws Exception {
        mockMvc.perform(get("/students/{id}", 99))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.detail").value("Aluno 99 não encontrado"));
    }

    @Test
    void putRenamesTheStudent() throws Exception {
        long id = postStudent("Ana");

        mockMvc.perform(put("/students/{id}", id).contentType(MediaType.APPLICATION_JSON).content("""
                        {"name": "Ana Souza"}
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Ana Souza"));
    }

    @Test
    void deleteRemovesTheStudentAndTheEnrollments() throws Exception {
        long id = postStudent("Ana");
        var bootcamp = new Bootcamp();
        bootcamp.setName("Java");
        bootcamp.setCreditHours(40);
        bootcamp.setStartDate(LocalDate.of(2024, 1, 8));
        bootcamp.setEndDate(LocalDate.of(2024, 3, 1));
        bootcamp.setStudents(List.of(studentRepository.findById(id).orElseThrow()));
        bootcampRepository.save(bootcamp);

        mockMvc.perform(delete("/students/{id}", id))
                .andExpect(status().isNoContent());

        assertEquals(0, studentRepository.count());
        assertEquals(1, bootcampRepository.count());
        assertEquals(0, jdbcTemplate.queryForObject("select count(*) from bootcamp_student", Integer.class));
    }

    private long postStudent(String name) throws Exception {
        String response = mockMvc.perform(post("/students").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"%s\"}".formatted(name)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        return ((Number) JsonPath.read(response, "$.id")).longValue();
    }

}
