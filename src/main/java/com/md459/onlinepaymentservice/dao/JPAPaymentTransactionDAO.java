/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.md459.onlinepaymentservice.dao;

import com.md459.onlinepaymentservice.entity.PaymentTransaction;
import com.md459.onlinepaymentservice.entity.SystemUser;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

/**
 *
 * @author marko
 */
@Stateless
public class JPAPaymentTransactionDAO implements PaymentTransactionDAO {
    
    @PersistenceContext
    private EntityManager em;

    @Override
    public long insert(PaymentTransaction transaction) {
        em.persist(transaction);
        return transaction.getId();
    }

    @Override
    public boolean update(PaymentTransaction transaction) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<PaymentTransaction> getAll() {
        TypedQuery<PaymentTransaction> query = em.createQuery(
            "SELECT t FROM PaymentTransaction t", PaymentTransaction.class);
        
        return query.getResultList();
    }

    @Override
    public List<PaymentTransaction> getHistory(SystemUser user) {
        TypedQuery<PaymentTransaction> query = em.createQuery(
            "SELECT t FROM PaymentTransaction t WHERE t.payer = :user OR t.payee = :user",
                PaymentTransaction.class);
        
        return query
                .setParameter("user", user)
                .getResultList();    
    }

    @Override
    public PaymentTransaction getById(long id) {
        TypedQuery<PaymentTransaction> query = em.createQuery(
            "SELECT t FROM PaymentTransaction t WHERE t.id = :id", PaymentTransaction.class);
        
        return query
                .setParameter("id", id)
                .getSingleResult();
    }

    @Override
    public List<PaymentTransaction> getPaymentRequests(SystemUser user) {
        TypedQuery<PaymentTransaction> query = em.createQuery(
            "SELECT t FROM PaymentTransaction t WHERE t.payer = :user AND t.status = :status",
                PaymentTransaction.class);
        
        return query
                .setParameter("user", user)
                .setParameter("status", "PENDING")
                .getResultList();
    }
    
}
