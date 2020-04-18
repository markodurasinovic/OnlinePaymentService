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
import javax.ejb.EJBException;
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
            "SELECT t FROM PaymentTransaction t WHERE t.payer = :user OR t.payee = :user",
                PaymentTransaction.class);
        
        return query
                .setParameter("user", user)
                .getResultList();
    }
    
    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void acceptRequest(long reqId) {
        PaymentTransaction transaction = getTransaction(reqId);
        
        if(transaction.getStatus().equals("PENDING")) {
            SystemUser fromUser = transaction.getPayer();
            SystemUser toUser = transaction.getPayee();

            float transferAmount = convert(toUser.getCurrency(), fromUser.getCurrency(), transaction.getAmount());
            if(fromUser.getBalance() >= transferAmount) {
                fromUser.setBalance(fromUser.getBalance() - transferAmount);
                toUser.setBalance(toUser.getBalance() + transaction.getAmount());

                transaction.setStatus("COMPLETE");
            } else {
                throw new EJBException("Insufficient funds.");
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
    public void requestPayment(String requester, String requestee, float amount, String description) {
        if(requester.equals(requestee)) throw new EJBException("User cannot request a payment from self.");
        
        SystemUser payee = usrSrv.getUser(requester);
        SystemUser payer = usrSrv.getUser(requestee);
        
        float transferAmount = convert(payer.getCurrency(), payee.getCurrency(), amount);
        PaymentTransaction transaction = new PaymentTransaction(
                payer, payee, transferAmount, description, payer.getCurrency());
        
        em.persist(transaction);
    }
    
    @Override
    public int getNumRequests(SystemUser user) {
        return getPaymentRequests(user).size();
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
    
    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void makePayment(String payerUsername, String payeeUsername, float amount, String description) {
        if(payerUsername.equals(payeeUsername)) throw new EJBException("User cannot make a payment to self");
        
        SystemUser payer = usrSrv.getUser(payerUsername);
        SystemUser payee = usrSrv.getUser(payeeUsername);        
        
        
        float transferAmount = convert(payer.getCurrency(), payee.getCurrency(), amount);
        PaymentTransaction transaction = new PaymentTransaction(
                payer, payee, transferAmount, description, payer.getCurrency());

        if(payer.getBalance() >= transferAmount) {

            payer.setBalance(payer.getBalance() - amount);
            payee.setBalance(payee.getBalance() + transferAmount);

            transaction.setStatus("COMPLETE");

            em.persist(transaction);
        } else {
            throw new EJBException("Insufficient funds.");
        }
    }
    
    private float convert(String currency1, String currency2, float amount) {
        return convMan.getConvertedAmount(currency1, currency2, amount);
    }
}
