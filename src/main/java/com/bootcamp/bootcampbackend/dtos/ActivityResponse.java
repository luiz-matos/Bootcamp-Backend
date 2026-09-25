package com.bootcamp.bootcampbackend.dtos;

import java.time.LocalDate;

public record ActivityResponse(Long id, String title, String description, LocalDate dateOfMentoring, double xp) {}
