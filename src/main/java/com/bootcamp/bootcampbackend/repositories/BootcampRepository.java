package com.bootcamp.bootcampbackend.repositories;

import com.bootcamp.bootcampbackend.entities.Bootcamp;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BootcampRepository extends JpaRepository<Bootcamp, Long> {

    boolean existsByName(String name);

    List<Bootcamp> findByStudentsId(Long studentId);
}
