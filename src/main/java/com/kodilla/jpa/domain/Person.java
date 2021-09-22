package com.kodilla.jpa.domain;

import javax.persistence.*;

@Entity
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstname;
    private String lastname;

    @ManyToOne
    @JoinColumn(name = "task_id")
    private Task task;

    @ManyToOne
    @JoinColumn(name = "subtask_id")
    private Subtask subtask;


    public Person(){}

    public Person(Long id, String firstname, String lastname, Task task, Subtask subtask) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.task = task;
        this.subtask = subtask;
    }

    public Person(Long id, String firstname, String lastname) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
    }

    public String getFirstname() {
        return firstname;
    }
}
