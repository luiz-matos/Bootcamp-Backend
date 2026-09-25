package com.bootcamp.bootcampbackend;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.bootcamp.bootcampbackend.entities.Bootcamp;
import com.bootcamp.bootcampbackend.entities.Student;
import com.bootcamp.bootcampbackend.repositories.BootcampRepository;
import com.bootcamp.bootcampbackend.repositories.StudentRepository;
import jakarta.persistence.EntityManagerFactory;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ActiveProfiles("test")
class DatabaseSchemaTest {

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private BootcampRepository bootcampRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Test
    void schemaIsUpdatedAndNeverDropped() {
        assertEquals("update", entityManagerFactory.getProperties().get("hibernate.hbm2ddl.auto"));
    }

    @Test
    void tablesFollowTheModel() {
        var tables = jdbcTemplate.queryForList(
                "select table_name from information_schema.tables where table_schema = 'public' order by table_name",
                String.class);

        assertEquals(
                List.of(
                        "activity",
                        "bootcamp",
                        "bootcamp_student",
                        "student",
                        "student_completed_activity",
                        "student_completed_bootcamp"),
                tables);
        assertEquals(List.of("bootcamp_id", "student_id"), columns("bootcamp_student"));
        assertEquals(List.of("id", "name"), columns("student"));
        assertEquals(List.of("bootcamp_id", "date_of_mentoring", "description", "id", "title"), columns("activity"));
    }

    @Test
    @Transactional
    void studentCanJoinSeveralBootcamps() {
        var student = new Student();
        student.setName("Ana");
        studentRepository.save(student);

        bootcampRepository.save(bootcampWith("Java", student));
        bootcampRepository.save(bootcampWith("Python", student));
        bootcampRepository.flush();

        assertEquals(
                2,
                jdbcTemplate.queryForObject(
                        "select count(*) from bootcamp_student where student_id = ?", Integer.class, student.getId()));
    }

    private List<String> columns(String table) {
        return jdbcTemplate.queryForList(
                "select column_name from information_schema.columns where table_name = ? order by column_name",
                String.class,
                table);
    }

    private Bootcamp bootcampWith(String name, Student student) {
        var bootcamp = new Bootcamp();
        bootcamp.setName(name);
        bootcamp.setCreditHours(40);
        bootcamp.setStartDate(LocalDate.of(2024, 1, 8));
        bootcamp.setEndDate(LocalDate.of(2024, 3, 1));
        bootcamp.setStudents(List.of(student));
        return bootcamp;
    }
}
