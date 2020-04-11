/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.md459.onlinepaymentservice.ejb;

import com.md459.onlinepaymentservice.entity.PaymentTransaction;
import com.md459.onlinepaymentservice.entity.SystemUser;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

/**
 *
 * @author marko
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class PaymentTransactionServiceBean implements PaymentTransactionService {
    
    @PersistenceContext
    private EntityManager em;
    
    @EJB
    UserService usrSrv;
    
    @EJB
    ConversionManager convMan;
    
    public PaymentTransactionServiceBean() {}
    
    @Override
    public List<PaymentTransaction> getAllTransactions() {
        TypedQuery<PaymentTransaction> query = em.createQuery(
            "SELECT t FROM PaymentTransaction t", PaymentTransaction.class);
        
        return query.getResultList();
    }
    
    @Override
    public List<PaymentTransaction> getTransactionHistory(SystemUser user) {
        TypedQuery<PaymentTransaction> query = em.createQuery(
            "SELECT t FROM PaymentTransaction t WHERE t.fromUser = :user OR t.toUser = :user",
                PaymentTransaction.class);
        
        return query
                .setParameter("user", user)
                .getResultList();
    }
    
    @Override
    public void acceptRequest(long reqId) {
        PaymentTransaction transaction = getTransaction(reqId);
        
        if(transaction.getStatus().equals("PENDING")) {
            SystemUser fromUser = transaction.getFromUser();
            SystemUser toUser = transaction.getToUser();

            float transferAmount = convert(toUser.getCurrency(), fromUser.getCurrency(), transaction.getAmount());
            if(fromUser.getBalance() >= transferAmount) {
                fromUser.setBalance(fromUser.getBalance() - transferAmount);
                toUser.setBalance(toUser.getBalance() + transaction.getAmount());

                transaction.setStatus("COMPLETE");
            } else {
                // throw exception here
            }
        }
    }
    
    @Override
    public void rejectRequest(long reqId) {
        PaymentTransaction transaction = getTransaction(reqId);
        
        if(transaction.getStatus().equals("PENDING")) {
            transaction.setStatus("VOID");
        }
    }
    
    private PaymentTransaction getTransaction(long id) {
        TypedQuery<PaymentTransaction> query = em.createQuery(
            "SELECT t FROM PaymentTransaction t WHERE t.id = :id", PaymentTransaction.class);
        
        return query
                .setParameter("id", id)
                .getSingleResult();
    }
    
    @Override
    public void requestPayment(String username, float amount, String description) {
        SystemUser toUser = usrSrv.getCurrentUser();
        SystemUser fromUser = usrSrv.getUser(username);
        
        float transferAmount = convert(fromUser.getCurrency(), toUser.getCurrency(), amount);
        PaymentTransaction transaction = new PaymentTransaction(fromUser, toUser, transferAmount, description);
        
        transaction.setStatus("PENDING");
        
        em.persist(transaction);
    }
    
    @Override
    public int getNumRequests(SystemUser user) {
        return getPaymentRequests(user).size();
    }
    
    @Override
    public List<PaymentTransaction> getPaymentRequests(SystemUser user) {
        TypedQuery<PaymentTransaction> query = em.createQuery(
            "SELECT t FROM PaymentTransaction t WHERE t.fromUser = :user AND t.status = :status",
                PaymentTransaction.class);
        
        return query
                .setParameter("user", user)
                .setParameter("status", "PENDING")
                .getResultList();
    }
    
    @Override
    public void makePayment(String username, float amount, String description) {
        SystemUser fromUser = usrSrv.getCurrentUser();
        SystemUser toUser = usrSrv.getUser(username);
        
        float transferAmount = convert(fromUser.getCurrency(), toUser.getCurrency(), amount);
        if(fromUser.getBalance() >= transferAmount) {
            PaymentTransaction transaction = new PaymentTransaction(fromUser, toUser, transferAmount, description);

            fromUser.setBalance(fromUser.getBalance() - amount);
            toUser.setBalance(toUser.getBalance() + transferAmount);

            transaction.setStatus("COMPLETE");

            em.persist(transaction);
        } else {
            // throw exception here
        }
    }
    
    private float convert(String currency1, String currency2, float amount) {
        return convMan.getConvertedAmount(currency1, currency2, amount);
    }
}
