package com.bootcamp.bootcampbackend.repositories;

import com.bootcamp.bootcampbackend.entities.Bootcamp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BootcampRepository extends JpaRepository<Bootcamp, Long> {
}
