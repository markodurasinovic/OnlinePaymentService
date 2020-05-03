package com.md459.onlinepaymentservice.dao;

import com.md459.onlinepaymentservice.dto.PaymentTransactionTO;
import com.md459.onlinepaymentservice.dto.SystemUserTO;
import com.md459.onlinepaymentservice.entity.PaymentTransaction;
import com.md459.onlinepaymentservice.entity.SystemUser;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

/**
 * JPA implementation of PaymentTransasctionDAO.
 */
@Stateless
public class JPAPaymentTransactionDAO implements PaymentTransactionDAO {
    
    @PersistenceContext
    private EntityManager em;

    /**
     * Insert a new PaymentTransaction entity into the DB based on the DTO values.
     * 
     * @param transaction - A PaymentTransaction DTO.
     */
    @Override
    public void insert(PaymentTransactionTO transaction) {
        SystemUser payer = em.find(SystemUser.class, transaction.payer.id);
        SystemUser payee = em.find(SystemUser.class, transaction.payee.id);
        
        PaymentTransaction trans = new PaymentTransaction(
            transaction.amount, transaction.description, transaction.currency, transaction.status);
        trans.setPayer(payer);
        trans.setPayee(payee);
        trans.setCreationTime(transaction.creationTime);
        
        em.persist(trans);
    }

    /**
     * Update an existing PaymentTransaction entity based on the DTO values.
     * 
     * @param transaction - A PaymentTransaction DTO.
     */
    @Override
    public void update(PaymentTransactionTO transaction) {
        PaymentTransaction trans = getTransactionById(transaction.id);        
        trans.setTransactionData(transaction);
    }

    /**
     * Get DTOs for all PaymentTransaction entities in the DB.
     * 
     * @return - A list of PaymentTransaction DTOs.
     */
    @Override
    public List<PaymentTransactionTO> getAll() {
        TypedQuery<PaymentTransaction> query = em.createQuery(
            "SELECT t FROM PaymentTransaction t", PaymentTransaction.class);
        
        List<PaymentTransaction> transactions = query.getResultList();
        
        return getTransactionData(transactions);
    }

    /**
     * Get DTOs for all PaymentTranaction entities in the DB related to user.
     * 
     * @param user - A SystemUser DTO.
     * @return - A list of PaymentTransaction DTOs.
     */
    @Override
    public List<PaymentTransactionTO> getHistory(SystemUserTO user) {
        TypedQuery<PaymentTransaction> query = em.createQuery(
            "SELECT t FROM PaymentTransaction t WHERE t.payer.id = :id OR t.payee.id = :id",
                PaymentTransaction.class);
        
        List<PaymentTransaction> transactions = query
                .setParameter("id", user.id)
                .getResultList();    
        
        return getTransactionData(transactions);
    }
    
    /**
     * Get DTOs for each payment request for a SystemUser. A payment request
     * is each transaction where Payer is user, and Status is PENDING.
     * 
     * @param user - A SystemUser DTO.
     * @return - A list of PaymentTransaction DTOs.
     */
    @Override
    public List<PaymentTransactionTO> getPaymentRequests(SystemUserTO user) {
        TypedQuery<PaymentTransaction> query = em.createQuery(
            "SELECT t FROM PaymentTransaction t WHERE t.payer.id = :id AND t.status = :status",
                PaymentTransaction.class);
        
        List<PaymentTransaction> transactions = query
                .setParameter("id", user.id)
                .setParameter("status", "PENDING")
                .getResultList();
        
        return getTransactionData(transactions);
    }
    
    /**
     * Get DTOs for each PaymentTransaction in transactions.
     * 
     * @param transactions - A list of PaymentTransaction entities.
     * @return - A list of PaymentTransaction DTOs.
     */
    private List<PaymentTransactionTO> getTransactionData(List<PaymentTransaction> transactions) {
        List<PaymentTransactionTO> transactionTOs = new ArrayList<>();
        transactions.forEach((t) -> {
            transactionTOs.add(t.getTransactionData());
        });
        
        return transactionTOs;
    }

    /**
     * Get a DTO for the PaymentTransaction entity corresponding to id.
     * 
     * @param id - A PaymentTransaction id.
     * @return - A PaymentTransaction DTO.
     */
    @Override
    public PaymentTransactionTO getById(long id) {
        PaymentTransaction trans = getTransactionById(id);
        
        return trans.getTransactionData();
    }
    
    /**
     * Get a PaymentTransaction entity corresponding to id.
     * 
     * @param id
     * @return - A PaymentTransaction entity.
     */
    private PaymentTransaction getTransactionById(long id) {
        TypedQuery<PaymentTransaction> query = em.createQuery(
            "SELECT t FROM PaymentTransaction t WHERE t.id = :id", PaymentTransaction.class);
        
        return query
                .setParameter("id", id)
                .getSingleResult();
    }
    
}
