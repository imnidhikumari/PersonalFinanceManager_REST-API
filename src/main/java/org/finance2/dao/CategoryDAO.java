package org.finance2.dao;

import org.finance2.core.Category;
import org.finance2.core.User;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.google.inject.Inject;
import org.hibernate.Transaction;

import java.util.List;


public class CategoryDAO {

    private final SessionFactory sessionFactory;

    @Inject
    public CategoryDAO(SessionFactory sessionFactory){
        this.sessionFactory = sessionFactory;
    }

    public void addCategory(Category category){
        Transaction transaction = null;
        try(Session session = sessionFactory.openSession()){
            transaction = session.beginTransaction();
            session.save(category);
            transaction.commit();
        } catch (HibernateException e) {
            if(transaction != null) transaction.rollback();
            throw new RuntimeException("Error registering user: ",e);
        }
    }


    public List<String> fetchAllCategoryForUser(Long userId) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("SELECT name FROM Category WHERE user.id LIKE :uid", String.class)
                    .setParameter("uid", userId)
                    .list();
        } catch (Exception e) {
            // Log the exception for debugging
            e.printStackTrace();
            throw new RuntimeException("Error fetching categories for user with ID: "+ userId, e);
        }
    }

}
