package ru.geekbrains.lesson3.hibernate.many_to_many;

import org.hibernate.cfg.Configuration;
import ru.geekbrains.lesson3.hibernate.entity.Book;
import ru.geekbrains.lesson3.hibernate.entity.Reader;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import java.util.ArrayList;
import java.util.List;

public class ManyToManyApp {
    public static void main(String[] args) {

        EntityManagerFactory factory = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(Book.class)
                .addAnnotatedClass(Reader.class)
                .buildSessionFactory();
        EntityManager em = factory.createEntityManager();

        clear(em);

        try {
            EntityTransaction transaction = em.getTransaction();
            transaction.begin();

            Reader reader1 = new Reader("One reader");
            Reader reader2 = new Reader("Two reader");

            Book book1 = new Book("One book");
            Book book2 = new Book("Two book");

            List<Reader> readerList = new ArrayList<>();
            readerList.add(reader1);
            readerList.add(reader2);

            List<Book> bookList = new ArrayList<>();
            bookList.add(book1);
            bookList.add(book2);

            reader1.setBooks(bookList);
            reader2.setBooks(bookList);

            book1.setReaders(readerList);
            book2.setReaders(readerList);

            em.persist(reader1);
            em.persist(reader2);

            transaction.commit();

            Reader reader = em.find(Reader.class, reader1.getId());
            System.out.println(reader);
            System.out.println("Books: ");
            for (Book b : reader.getBooks()) {
                System.out.println(b.getTitle());
            }

            List<Reader> readers = em.createQuery("SELECT r FROM Reader r ORDER BY size(r.books) DESC").getResultList();
            System.out.println(readers);

            transaction.begin();
            em.createQuery("DELETE FROM Book b WHERE b.id = 65").executeUpdate();
            transaction.commit();

            List<Book> books = em.createQuery("FROM Book").getResultList();
            System.out.println(books);

        } finally {
            factory.close();
            if (em != null) {
                em.close();
            }
        }
    }

    private static void clear(EntityManager em) {
        em.getTransaction().begin();
        em.createQuery("DELETE FROM Reader").executeUpdate();
        em.createQuery("DELETE FROM Book").executeUpdate();
        em.getTransaction().commit();
    }
}
