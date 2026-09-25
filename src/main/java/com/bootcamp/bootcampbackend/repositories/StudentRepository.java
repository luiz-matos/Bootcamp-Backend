package com.bootcamp.bootcampbackend.repositories;

import com.bootcamp.bootcampbackend.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {

    boolean existsByCompletedActivitiesId(Long activityId);
}
