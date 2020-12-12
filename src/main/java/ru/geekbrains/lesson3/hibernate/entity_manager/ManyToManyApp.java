package ru.geekbrains.lesson3.hibernate.entity_manager;

import org.hibernate.annotations.common.reflection.ClassLoadingException;
import org.hibernate.cfg.Configuration;
import ru.geekbrains.lesson3.hibernate.entity.Person;
import ru.geekbrains.lesson3.hibernate.entity.Product;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import java.util.ArrayList;
import java.util.List;

public class ManyToManyApp {
    public static void main(String[] args) {

            EntityManagerFactory factory = new Configuration()
                    .configure("hibernate.cfg.xml")
                    .addAnnotatedClass(Product.class)
                    .addAnnotatedClass(Person.class)
                    .buildSessionFactory();

            EntityManager em = factory.createEntityManager();

            //clear(em);

            try {
                EntityTransaction transaction = em.getTransaction();
                transaction.begin();

                Person person1 = new Person("One Person");
                Person person2 = new Person("Two Person");

                Product product1 = new Product("One product");
                Product product2 = new Product("Two product");

                List<Person> personList = new ArrayList<>();
                personList.add(person1);
                personList.add(person2);

                List<Product> productList = new ArrayList<>();
                productList.add(product1);
                productList.add(product2);

                person1.setProducts(productList);
                person2.setProducts(productList);

                product1.setPersons(personList);
                product2.setPersons(personList);

                em.persist(person1);
                em.persist(person2);

                transaction.commit();

                Person person = em.find(Person.class, person1.getId());
                System.out.println(person);
                System.out.println("Products: ");
                for (Product b : person.getProducts()) {
                    System.out.println(b.getTitle());
                }

                List<Person> persons = em.createQuery("SELECT r FROM Person r ORDER BY size(r.products) DESC").getResultList();
                System.out.println("================");
                System.out.println(persons);

                transaction.begin();
                //em.createQuery("DELETE FROM Book b WHERE b.id = 65").executeUpdate();
                transaction.commit();

                //List<Product> products = em.createQuery("FROM Book").getResultList();
                //System.out.println(products);

            } finally {
                factory.close();
                if (em != null) {
                    em.close();
                }
            }
    }

    private static void clear (EntityManager em){
        em.getTransaction().begin();
        em.createQuery("DELETE FROM Person").executeUpdate();
        em.createQuery("DELETE FROM Product").executeUpdate();
        em.getTransaction().commit();
    }
}
