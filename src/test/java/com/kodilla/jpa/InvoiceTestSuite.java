package com.kodilla.jpa;

import com.kodilla.jpa.domain.Customer;
import com.kodilla.jpa.domain.Invoice;
import com.kodilla.jpa.domain.Item;
import com.kodilla.jpa.domain.Product;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.*;

@SpringBootTest
class InvoiceTestSuite {

    @PersistenceUnit
    private EntityManagerFactory emf;

    @Test
    void shouldNPlusOneProblemOccure() {
        //Given
        List<Long> savedInvoices = insertExampleData();
        EntityManager em = emf.createEntityManager();

        //When
        System.out.println("****************** BEGIN OF FETCHING *******************");
        System.out.println("*** STEP 1 – query for invoices ***");

        TypedQuery<Invoice> query = em.createQuery(
                "from Invoice "
                        + " where id in (" + invoiceIds(savedInvoices) + ")",
                Invoice.class);

        EntityGraph<Invoice> eg = em.createEntityGraph(Invoice.class);
        eg.addAttributeNodes("customer");
        eg.addSubgraph("items").addAttributeNodes("product");
        query.setHint("javax.persistence.fetchgraph", eg);

        List<Invoice> invoices =
                query.getResultList();

        for (Invoice invoice : invoices) {
            System.out.println("*** STEP 2 – read data from invoice ***");
            System.out.println(invoice);
            System.out.println("*** STEP 3 – read the customer data ***");
            System.out.println(invoice.getCustomer());

            for (Item item : invoice.getItems()) {
                System.out.println("*** STEP 4 – read the item ***");
                System.out.println(item);
                System.out.println("*** STEP 5 – read the product from item ***");
                System.out.println(item.getProduct());
            }

        }

        System.out.println("****************** END OF FETCHING *******************");

        //Then
        //Here should be some assertions and the clean up performed

    }

    private String invoiceIds(List<Long> invoiceIds) {
        return invoiceIds.stream()
                .map(n -> "" + n)
                .collect(Collectors.joining(","));
    }

    private List<Long> insertExampleData() {
        Customer c1 = new Customer(null, "Customer1", "c1_address1", "c1_address2");
        Customer c2 = new Customer(null, "Customer2", "c2_address1", "c2_address2");

        Invoice iv1 = new Invoice(null, "1/2020", LocalDate.now(), c1);
        Invoice iv2 = new Invoice(null, "2/2020", LocalDate.now(), c2);
        Product p1 = new Product(null, "Product1");
        Product p2 = new Product(null, "Product2");
        Product p3 = new Product(null, "Product3");
        Product p4 = new Product(null, "Product4");
        Item item1Invoice1 = new Item(null, 1, 100, iv1, p1);
        Item item2Invoice1 = new Item(null, 1, 200, iv1, p2);
        Item item3Invoice2 = new Item(null, 2, 50, iv2, p3);
        Item item4Invoice2 = new Item(null, 3, 10, iv2, p4);
        iv1.getItems().addAll(List.of(item1Invoice1, item2Invoice1));
        iv2.getItems().addAll(List.of(item3Invoice2, item4Invoice2));
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.persist(p1);
        em.persist(p2);
        em.persist(p3);
        em.persist(p4);
        em.persist(c1);
        em.persist(c2);
        em.persist(iv1);
        em.persist(iv2);
        em.persist(item1Invoice1);
        em.persist(item2Invoice1);
        em.persist(item3Invoice2);
        em.persist(item4Invoice2);
        em.flush();
        em.getTransaction().commit();
        em.close();

        return List.of(iv1.getId(), iv2.getId());
    }

}