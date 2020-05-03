/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
 *
 * @author marko
 */
@Stateless
public class JPAPaymentTransactionDAO implements PaymentTransactionDAO {
    
    @PersistenceContext
    private EntityManager em;

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

    @Override
    public void update(PaymentTransactionTO transaction) {
        PaymentTransaction trans = getTransactionById(transaction.id);        
        trans.setTransactionData(transaction);
    }

    @Override
    public List<PaymentTransactionTO> getAll() {
        TypedQuery<PaymentTransaction> query = em.createQuery(
            "SELECT t FROM PaymentTransaction t", PaymentTransaction.class);
        
        List<PaymentTransaction> transactions = query.getResultList();
        
        return getTransactionData(transactions);
    }

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
    
    private List<PaymentTransactionTO> getTransactionData(List<PaymentTransaction> transactions) {
        List<PaymentTransactionTO> transactionTOs = new ArrayList<>();
        transactions.forEach((t) -> {
            transactionTOs.add(t.getTransactionData());
        });
        
        return transactionTOs;
    }

    @Override
    public PaymentTransactionTO getById(long id) {
        PaymentTransaction trans = getTransactionById(id);
        
        return trans.getTransactionData();
    }
    
    private PaymentTransaction getTransactionById(long id) {
        TypedQuery<PaymentTransaction> query = em.createQuery(
            "SELECT t FROM PaymentTransaction t WHERE t.id = :id", PaymentTransaction.class);
        
        return query
                .setParameter("id", id)
                .getSingleResult();
    }

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
    
}
