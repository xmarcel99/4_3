package com.kodilla.jpa.domain;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String status;


    @OneToMany(targetEntity = Person.class, mappedBy = "task")
    private Set<Person> person;

    @OneToMany(targetEntity = Subtask.class, mappedBy = "task")
    private Set<Subtask> subtask;

    public Task(){}

    public Task(Long id, String name, String status, Set<Person> person, Set<Subtask> subtask) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.person = person;
        this.subtask = subtask;
    }

    public Task(Long id, String name, String status) {
        this.id = id;
        this.name = name;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getStatus() {
        return status;
    }

    public Set<Person> getPerson() {
        return person;
    }

    public Set<Subtask> getSubtask() {
        return subtask;
    }
}
