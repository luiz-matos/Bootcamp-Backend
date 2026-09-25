package com.bootcamp.bootcampbackend;

import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
class DatabaseSchemaTest {

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Test
    void schemaIsUpdatedAndNeverDropped() {
        assertEquals("update", entityManagerFactory.getProperties().get("hibernate.hbm2ddl.auto"));
    }

}
