package com.bootcamp.bootcampbackend.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.jayway.jsonpath.JsonPath;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

/** Base dos testes da API: aplicação inteira com H2 novo a cada teste e atalhos para criar os dados. */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
abstract class ApiTest {

    @Autowired
    protected MockMvc mockMvc;

    protected long postBootcamp(String name) throws Exception {
        return postBootcampJson("""
                {"name": "%s", "description": "Trilha de back-end", "creditHours": 40,
                 "startDate": "2024-01-08", "endDate": "2024-03-01"}
                """.formatted(name));
    }

    protected long postBootcampJson(String json) throws Exception {
        return create(post("/bootcamps"), json);
    }

    protected long postStudent(String name) throws Exception {
        return create(post("/students"), "{\"name\": \"%s\"}".formatted(name));
    }

    protected long postActivity(long bootcampId, String title) throws Exception {
        return create(
                post("/bootcamps/{id}/activities", bootcampId),
                "{\"title\": \"%s\", \"dateOfMentoring\": \"2024-01-15\"}".formatted(title));
    }

    protected long idOf(String json) {
        return ((Number) JsonPath.read(json, "$.id")).longValue();
    }

    private long create(MockHttpServletRequestBuilder request, String json) throws Exception {
        return idOf(
                mockMvc.perform(request.contentType(MediaType.APPLICATION_JSON).content(json))
                        .andExpect(status().isCreated())
                        .andReturn()
                        .getResponse()
                        .getContentAsString());
    }
}
