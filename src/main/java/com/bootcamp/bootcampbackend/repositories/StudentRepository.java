package com.bootcamp.bootcampbackend.repositories;

import com.bootcamp.bootcampbackend.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
}
