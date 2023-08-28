package com.examly.hibernate;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import com.examly.hibernate.Model.Address;
import com.examly.hibernate.Model.Person;

public class HibernateApplication {
    public static void main(String[] args) {
        // Create a Hibernate session factory
        SessionFactory sessionFactory = new Configuration()
            .configure("hibernate.cfg.xml")
            .addAnnotatedClass(Person.class)
            .addAnnotatedClass(Address.class)
            .buildSessionFactory();
        
        // Create a Hibernate session
		Session session = sessionFactory.openSession();
        
        try {
            // Begin a transaction
            session.beginTransaction();
            
            // Create a person and an address
            Person person = new Person("John Doe");
            Address address = new Address("123 Main St", "Cityville");
            
            // Associate the address with the person
            person.setAddress(address);
            address.setPerson(person);
            
            // Save the person (and cascade the address)
            session.save(person);
            
            // Commit the transaction
            session.getTransaction().commit();
            
            // Display data
            displayData(session);
        } finally {
            // Clean up resources
            session.close();
            sessionFactory.close();
        }
    }
    
    private static void displayData(Session session) {
        // Retrieve and display data
        System.out.println("Displaying Data:");
        session.beginTransaction();
        
        // Query to fetch all persons
        List<Person> persons = session.createQuery("from Person", Person.class).getResultList();
        
        for (Person person : persons) {
            System.out.println("Person: " + person.getName());
            Address address = person.getAddress();
            System.out.println("Address: " + address.getStreet() + ", " + address.getCity());
        }
        
        session.getTransaction().commit();
    }
}
