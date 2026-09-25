package com.bootcamp.bootcampbackend.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
public class Activity {

    private static final double DEFAULT_XP = 15d;

    @Id
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O título é obrigatório")
    @Column
    private String title;

    @Column
    private String description;

    @NotNull(message = "A data da mentoria é obrigatória")
    @Column
    private LocalDate dateOfMentoring;

    @JsonIgnore
    @ManyToOne
    private Bootcamp bootcamp;

    @JsonProperty("xp")
    public double xpCalculate() {
        return DEFAULT_XP + 20d;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDateOfMentoring() {
        return dateOfMentoring;
    }

    public void setDateOfMentoring(LocalDate dateOfMentoring) {
        this.dateOfMentoring = dateOfMentoring;
    }

    public Bootcamp getBootcamp() {
        return bootcamp;
    }

    public void setBootcamp(Bootcamp bootcamp) {
        this.bootcamp = bootcamp;
    }
}
