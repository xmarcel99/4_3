package com.kodilla.jpa.domain;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
public class Subtask {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String status;


    @OneToMany(targetEntity = Person.class, mappedBy = "subtask")
    private Set<Person> person;

    public Subtask(Long id, String name, String status, Task task) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.task = task;
    }

    @ManyToOne
    @JoinColumn(name = "task_id")
    private Task task;

    public Subtask(){}



    public Subtask(Long id, String name, String status, Set<Person> person) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.person = person;
    }
}
