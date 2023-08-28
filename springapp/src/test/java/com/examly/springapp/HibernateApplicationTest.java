package com.examly.hibernate;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import com.examly.hibernate.Model.Address;
import com.examly.hibernate.Model.Person;

public class HibernateApplicationTest {

    private SessionFactory sessionFactory;
    
    @Before
    public void setup() {
        sessionFactory = new Configuration()
            .configure("hibernate.cfg.xml")
            .addAnnotatedClass(Person.class)
            .addAnnotatedClass(Address.class)
            .buildSessionFactory();
    }
    
    @After
    public void cleanup() {
        sessionFactory.close();
    }
    
    @Test
    public void testSaveAndRetrieveData() {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        Person person = new Person("John Doe");
        Address address = new Address("123 Main St", "Cityville");

        person.setAddress(address);
        address.setPerson(person);

        session.save(person);
        transaction.commit();
        session.close();

        session = sessionFactory.openSession();
        transaction = session.beginTransaction();

        Person retrievedPerson = session.get(Person.class, person.getId());

        assertEquals("John Doe", retrievedPerson.getName());
        assertEquals("123 Main St", retrievedPerson.getAddress().getStreet());
        assertEquals("Cityville", retrievedPerson.getAddress().getCity());

        transaction.commit();
        session.close();
    }
}
