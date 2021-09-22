package com.kodilla.jpa;

import com.kodilla.jpa.domain.*;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class TaskTestSuite {

    @PersistenceUnit
    private EntityManagerFactory emf;

    @Test
    void shouldPersistCustomer() {
        //Given
        EntityManager em = emf.createEntityManager();
        Task task =
                new Task(null,"task1","testStatus", Set.of(new Person(null, "testName", "testLastName")),Set.of(new Subtask(null,"test3443","test2342", Set.of(new Person(null, "testName", "testLastName")))));

        //When
        em.getTransaction().begin();
        em.persist(task);
        em.flush();
        em.getTransaction().commit();

        //Then
        Long key = task.getId();
        Task task1 = em.find(Task.class, key);
        em.close();
        assertThat(task1.getName()).isEqualTo(task.getName());
    }


    private List<Long> insertExampleData() {
        Task c1 = new Task(null, "N1","S1");
        Subtask s1 = new Subtask(null,"name","firstname",c1);
        Person p1 = new Person(null,"name1","lastname1",c1,s1);
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.persist(p1);
        em.persist(s1);
        em.persist(c1);
        em.flush();
        em.getTransaction().commit();
        em.close();

        return List.of(c1.getId());
    }

    private String getId(List<Long> invoiceIds) {
        return invoiceIds.stream()
                .map(n -> "" + n)
                .collect(Collectors.joining(","));
    }

    @Test
    void testAfterGraph() {
        //Given
        List<Long> savedInvoices = insertExampleData();
        EntityManager em = emf.createEntityManager();

        //When
        System.out.println("****************** BEGIN OF FETCHING *******************");
        System.out.println("*** STEP 1 – query for invoices ***");

        TypedQuery<Task> query = em.createQuery(
                "from Task "
                        + " where id in (" + getId(savedInvoices) + ")",
                Task.class);

        EntityGraph<Task> eg = em.createEntityGraph(Task.class);
        eg.addSubgraph("person").addAttributeNodes("task","subtask");
        eg.addSubgraph("subtask").addAttributeNodes("person");

        query.setHint("javax.persistence.fetchgraph", eg);
        List<Task> tasks = query.getResultList();

        for (Task task : tasks) {
            System.out.println("*** STEP 2 – read data from task ***");
            System.out.println(task);
            System.out.println("*** STEP 3 – read the name of task ***");
            System.out.println(task.getName());

            for (Person person : task.getPerson()) {
                System.out.println("*** STEP 4 – read the person ***");
                System.out.println(person);
                System.out.println("*** STEP 5 – read the person name ***");
                System.out.println(person.getFirstname());
            }

        }

        System.out.println("****************** END OF FETCHING *******************");

        //Then
        //Here should be some assertions and the clean up performed

    }

}