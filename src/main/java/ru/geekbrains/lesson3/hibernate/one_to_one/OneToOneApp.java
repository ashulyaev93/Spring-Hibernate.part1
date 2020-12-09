package ru.geekbrains.lesson3.hibernate.one_to_one;

import org.hibernate.cfg.Configuration;
import ru.geekbrains.lesson3.hibernate.entity.Person;
import ru.geekbrains.lesson3.hibernate.entity.PersonDetails;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;

public class OneToOneApp {

    public static void main(String[] args) {

        EntityManagerFactory factory = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(Person.class)
                .addAnnotatedClass(PersonDetails.class)
                .buildSessionFactory();
        EntityManager em = factory.createEntityManager();

        clear(em);

        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();

            PersonDetails personDetails1 = new PersonDetails("description...1", "923478324");
            PersonDetails personDetails2 = new PersonDetails("description...2", "565423421");

            Person person1 = new Person("Alex", "test1@test.com", personDetails1);
            Person person2 = new Person("Alena", "test2@test.com", personDetails2);

            em.persist(person1);
            em.persist(person2);

            transaction.commit();

            Person person = em.find(Person.class, person1.getId());
            System.out.println(person);
        } finally {
            factory.close();
            if (em != null) {
                em.close();
            }
        }
    }

    private static void clear(EntityManager em) {
        em.getTransaction().begin();
        em.createQuery("DELETE FROM Person ").executeUpdate();
        em.createQuery("DELETE FROM PersonDetails ").executeUpdate();
        em.getTransaction().commit();
    }
}
