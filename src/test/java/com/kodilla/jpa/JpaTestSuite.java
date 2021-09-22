package com.kodilla.jpa;

import com.kodilla.jpa.domain.Customer;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class JpaTestSuite {

    @PersistenceUnit
    private EntityManagerFactory emf;

    @Test
    void shouldPersistCustomer() {
        //Given
        EntityManager em = emf.createEntityManager();
        Customer kodilla =
                new Customer(null, "Kodilla", "ul. Racławicka 13", "Wrocław");

        //When
        em.getTransaction().begin();
        em.persist(kodilla);
        em.flush();
        em.getTransaction().commit();

        //Then
        Long key = kodilla.getId();
        Customer readCustomer = em.find(Customer.class, key);
        em.close();
        assertThat(readCustomer.getName()).isEqualTo(kodilla.getName());
    }

}