package ru.geekbrains.lesson3.hibernate.crud;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import ru.geekbrains.lesson3.hibernate.entity.SimpleItem;

public class CrudApp {
    public static void main(String[] args) {
        SessionFactory factory = new Configuration()
                .configure("hibernate.cfg.xml")
                .buildSessionFactory();

        //defaultValues(factory);
        Session session = factory.getCurrentSession();
        try {
            System.out.println("============\n== CREATE ==\n============");
            session.beginTransaction();
            SimpleItem newSimpleItem = new SimpleItem("Apple", 100);
            System.out.println("Before save: " + newSimpleItem);
            session.save(newSimpleItem);
            System.out.println("After save: " + newSimpleItem);
            session.getTransaction().commit();
            System.out.println("After save and commit: " + newSimpleItem);

            System.out.println("============\n=== READ ===\n============");
            session = factory.getCurrentSession();
            session.beginTransaction();
            SimpleItem simpleItemFromDb1 = session.get(SimpleItem.class, 1L);
            SimpleItem simpleItemFromDb2 = session.get(SimpleItem.class, 1L);
            System.out.println(simpleItemFromDb1);
            System.out.println(simpleItemFromDb2);

            session.getTransaction().commit();

            System.out.println("============\n=== READ IN ANOTHER SESSION ===\n============");
            session = factory.getCurrentSession();
            session.beginTransaction();
            SimpleItem simpleItemFromDb3 = session.get(SimpleItem.class, 1L);
            System.out.println(simpleItemFromDb3);
            session.getTransaction().commit();

            System.out.println("============\n== UPDATE ==\n============");
            session = factory.getCurrentSession();
            session.beginTransaction();
            Long maxId = session.createQuery("SELECT MAX(s.id) FROM SimpleItem s", Long.class).getSingleResult();
            SimpleItem simpleItemForUpdate = session.createQuery("SELECT s FROM SimpleItem s WHERE s.id = :id", SimpleItem.class)
                    .setParameter("id", maxId)
                    .getSingleResult();
            System.out.println("Loaded item with max(id): " + simpleItemForUpdate);
            simpleItemForUpdate.setPrice(simpleItemForUpdate.getPrice() + 1100000);
            System.out.println("Modified item: " + simpleItemForUpdate);
            session.getTransaction().commit();

            session = factory.getCurrentSession();
            session.beginTransaction();
            SimpleItem simpleItemAfterUpdate = session.get(SimpleItem.class, simpleItemForUpdate.getId());
            System.out.println("Loaded item after update: " + simpleItemAfterUpdate);
            session.getTransaction().commit();

            System.out.println("============\n== DELETE ==\n============");
            session = factory.getCurrentSession();
            session.beginTransaction();
            maxId = session.createQuery("SELECT MAX(s.id) FROM SimpleItem s", Long.class).getSingleResult();
            session.delete(session.get(SimpleItem.class, maxId));
            session.getTransaction().commit();

            session = factory.getCurrentSession();
            session.beginTransaction();
            SimpleItem removedSimpleItem = session.get(SimpleItem.class, maxId);
            System.out.println("Loaded item after remove: " + removedSimpleItem);
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            factory.close();
            if (session != null) {
                session.close();
            }
        }
    }

    private static void defaultValues(SessionFactory sessionFactory) {
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        SimpleItem newSimpleItem1 = new SimpleItem("Apple", 100);
        SimpleItem newSimpleItem2 = new SimpleItem("Orange", 200);
        SimpleItem newSimpleItem3 = new SimpleItem("Pineapple", 300);
        SimpleItem newSimpleItem4 = new SimpleItem("Pear", 400);
        SimpleItem newSimpleItem5 = new SimpleItem("Mango", 500);
        session.save(newSimpleItem1);
        session.save(newSimpleItem2);
        session.save(newSimpleItem3);
        session.save(newSimpleItem4);
        session.save(newSimpleItem5);
        session.getTransaction().commit();
    }
}
