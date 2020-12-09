package ru.geekbrains.lesson3.hibernate.jpql;

import org.hibernate.cfg.Configuration;
import ru.geekbrains.lesson3.hibernate.entity.SimpleItem;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public class JPQLApp {

    public static void main(String[] args) {
        EntityManagerFactory factory = new Configuration()
                .configure("hibernate.cfg.xml")
                .buildSessionFactory();
        EntityManager em = factory.createEntityManager();

        try {
            EntityTransaction transaction = em.getTransaction();
            transaction.begin();
            SimpleItem newSimpleItem = new SimpleItem("Orange", 100);
            em.persist(newSimpleItem);
            transaction.commit();

            SimpleItem simpleItemFromDb = em.find(SimpleItem.class, newSimpleItem.getId());
            System.out.println("simpleItemFromDb = " + simpleItemFromDb);

            String queryString = "SELECT s FROM SimpleItem s WHERE s.id = :id";
            TypedQuery<SimpleItem> typedQuery = em.createQuery(queryString, SimpleItem.class);
            typedQuery.setParameter("id", newSimpleItem.getId());
            SimpleItem simpleItem = typedQuery.getSingleResult();
            System.out.println("simpleItem = " + simpleItem);

            TypedQuery<SimpleItem> query = em.createQuery("FROM SimpleItem", SimpleItem.class);
            List<SimpleItem> simpleItemList = query.getResultList();
            System.out.println("simpleItemList = " + simpleItemList);

            Query query2 = em.createQuery("SELECT COUNT(*) FROM SimpleItem");
            Number count = (Number) query2.getSingleResult();
            System.out.println("count = " + count);

            CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
            CriteriaQuery<SimpleItem> criteriaBuilderQuery = criteriaBuilder.createQuery(SimpleItem.class);
            Root<SimpleItem> personRoot = criteriaBuilderQuery.from(SimpleItem.class);
            criteriaBuilderQuery.select(personRoot);
            criteriaBuilderQuery.where(criteriaBuilder.equal(personRoot.get("id"), 36l));
            em.createQuery(criteriaBuilderQuery)
                    .getResultList()
                    .forEach(System.out::println);

            List<SimpleItem> simpleItems = em.createNamedQuery("SimpleItem.findAll", SimpleItem.class).getResultList();
            System.out.println("simpleItems : " + simpleItems);
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
}
