package org.finance2.dao;

import org.finance2.core.User;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.google.inject.Inject;
import org.hibernate.Transaction;


public class UserDAO {

    private final SessionFactory sessionFactory;

    @Inject
    public UserDAO(SessionFactory sessionFactory){
        this.sessionFactory = sessionFactory;
    }

    public void registerUser(User user){
        Transaction transaction = null;
        try(Session session = sessionFactory.openSession()){
            transaction = session.beginTransaction();
            session.save(user);
            transaction.commit();
        } catch (HibernateException e) {
            if(transaction != null) transaction.rollback();
            throw new RuntimeException("Error registering user: ",e);
        }
    }

}
