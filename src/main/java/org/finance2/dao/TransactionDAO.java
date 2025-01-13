package org.finance2.dao;

import org.finance2.core.TransactionTable;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.google.inject.Inject;
import org.hibernate.Transaction;

import java.util.List;


public class TransactionDAO {

    private final SessionFactory sessionFactory;

    @Inject
    public TransactionDAO(SessionFactory sessionFactory){
        this.sessionFactory = sessionFactory;
    }

    public void addTransaction(TransactionTable transactionTable){
        Transaction transaction = null;
        try(Session session = sessionFactory.openSession()){
            transaction = session.beginTransaction();
            session.save(transactionTable);
            transaction.commit();
        } catch (HibernateException e) {
            if(transaction != null) transaction.rollback();
            throw new RuntimeException("Error registering user: ",e);
        }
    }

    public List<TransactionTable> getAllTransaction() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM TransactionTable", TransactionTable.class).list();
        } catch (HibernateException e) {
            throw new RuntimeException("Error getting all transactions", e);
        }
    }

    public List<TransactionTable> getTransactionByUserId(Long userId){
        try(Session session=sessionFactory.openSession()){
            return session.createQuery("FROM TransactionTable t WHERE t.userId= :userId",TransactionTable.class)
                    .setParameter("userId", userId)
                    .list();
        } catch (HibernateException e) {
            throw new RuntimeException("Error getting transaction by id:" + userId, e);
        }
    }

}
