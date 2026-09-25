package com.bootcamp.bootcampbackend.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "student_completed_activity",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "activity_id"))
    private List<Activity> completedActivities = new ArrayList<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "student_completed_bootcamp",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "bootcamp_id"))
    private Set<Bootcamp> completedBootcamps = new HashSet<>();

    public double getXp() {
        return completedActivities.stream().mapToDouble(Activity::xpCalculate).sum()
                + completedBootcamps.stream().mapToDouble(Bootcamp::xpCalculate).sum();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Activity> getCompletedActivities() {
        return completedActivities;
    }

    public void setCompletedActivities(List<Activity> completedActivities) {
        this.completedActivities = completedActivities;
    }

    public Set<Bootcamp> getCompletedBootcamps() {
        return completedBootcamps;
    }

    public void setCompletedBootcamps(Set<Bootcamp> completedBootcamps) {
        this.completedBootcamps = completedBootcamps;
    }
}
