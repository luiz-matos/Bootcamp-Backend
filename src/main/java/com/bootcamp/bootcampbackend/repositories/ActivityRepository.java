package com.bootcamp.bootcampbackend.repositories;

import com.bootcamp.bootcampbackend.entities.Activity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActivityRepository extends JpaRepository<Activity, Long> {
}
