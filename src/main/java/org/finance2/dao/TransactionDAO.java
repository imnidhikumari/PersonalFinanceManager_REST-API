package org.finance2.dao;

import org.finance2.core.Category;
import org.finance2.core.TransactionTable;
import org.finance2.core.User;
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
            // Fetch the User from the database
            User user = session.get(User.class, transactionTable.getUser().getId());
            if (user == null) {
                throw new IllegalArgumentException("User does not exist");
            }
            transactionTable.setUser(user);

            // Fetch the Category from the database
            Category category = session.get(Category.class, transactionTable.getCategory().getUser().getId());
            if (category == null) {
                throw new IllegalArgumentException("Category does not exist");
            }
            transactionTable.setCategory(category);

            // Save the transaction
            session.save(transactionTable);
            transaction.commit();
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException("Error registering transaction: ", e);
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
            return session.createQuery("FROM TransactionTable t WHERE t.user.id= :userId",TransactionTable.class)
                    .setParameter("userId", userId)
                    .list();
        } catch (HibernateException e) {
            throw new RuntimeException("Error getting transaction by id:" + userId, e);
        }
    }

}
