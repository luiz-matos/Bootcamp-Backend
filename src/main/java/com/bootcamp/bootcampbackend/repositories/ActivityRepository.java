package com.bootcamp.bootcampbackend.repositories;

import com.bootcamp.bootcampbackend.entities.Activity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActivityRepository extends JpaRepository<Activity, Long> {

    List<Activity> findByBootcampId(Long bootcampId);

    Optional<Activity> findByIdAndBootcampId(Long id, Long bootcampId);
}
