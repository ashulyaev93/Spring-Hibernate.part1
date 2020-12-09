package ru.geekbrains.lesson3.hibernate.one_to_many;

import org.hibernate.cfg.Configuration;
import ru.geekbrains.lesson3.hibernate.entity.Category;
import ru.geekbrains.lesson3.hibernate.entity.Product;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;

public class OneToManyApp {
    public static void main(String[] args) {

        EntityManagerFactory factory = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(Category.class)
                .addAnnotatedClass(Product.class)
                .buildSessionFactory();
        EntityManager em = factory.createEntityManager();

        clear(em);
        try {
            EntityTransaction transaction = em.getTransaction();
            transaction.begin();

            Category category = new Category();
            category.setTitle("new Category");

            Product product1 = new Product("one Product", category);
            Product product2 = new Product("two Product", category);
            List<Product> productList = new ArrayList<>();
            productList.add(product1);
            productList.add(product2);

            category.setProducts(productList);

            em.persist(category);
            transaction.commit();

//            TypedQuery<Category> fromCategory = em.createQuery("FROM Category", Category.class);
//            fromCategory.getResultList().stream().forEach(Category::toString);

            Category categoryWithProducts = em.createNamedQuery("withProducts", Category.class)
                    .setParameter("id", category.getId())
                    .getSingleResult();
            
            System.out.println(categoryWithProducts.getProducts());

        } finally {
            factory.close();
            if (em != null) {
                em.close();
            }
        }
    }

    private static void clear(EntityManager em) {
        em.getTransaction().begin();
        em.createQuery("DELETE FROM Product").executeUpdate();
        em.createQuery("DELETE FROM Category").executeUpdate();
        em.getTransaction().commit();
    }
}
