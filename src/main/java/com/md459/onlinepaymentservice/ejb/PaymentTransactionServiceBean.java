/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.md459.onlinepaymentservice.ejb;

import com.md459.onlinepaymentservice.dao.PaymentTransactionDAO;
import com.md459.onlinepaymentservice.entity.PaymentTransaction;
import com.md459.onlinepaymentservice.entity.SystemUser;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

/**
 *
 * @author marko
 */
@Stateless
public class PaymentTransactionServiceBean implements PaymentTransactionService {
        
    @EJB
    PaymentTransactionDAO transDAO;
    
    @EJB
    UserService usrSrv;
    
    @EJB
    ConversionManager convMan;
    
    public PaymentTransactionServiceBean() {}
    
    @Override
    public List<PaymentTransaction> getAllTransactions() {
        return transDAO.getAll();
    }
    
    @Override
    public List<PaymentTransaction> getTransactionHistory(SystemUser user) {
        return transDAO.getHistory(user);
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
        return transDAO.getById(id);
    }
    
    @Override
    public void requestPayment(String requester, String requestee, float amount, String description) {
        if(requester.equals(requestee)) throw new EJBException("User cannot request a payment from self.");
        
        SystemUser payee = usrSrv.getUser(requester);
        // Exception thrown if no user is found with username requestee.
        SystemUser payer;
        if(usrSrv.hasUser(requestee)) {
            payer = usrSrv.getUser(requestee);
        } else {
            throw new EJBException("User does not exist.");
        }
        
        float transferAmount = convert(payer.getCurrency(), payee.getCurrency(), amount);
        PaymentTransaction transaction = new PaymentTransaction(
                payer, payee, transferAmount, description, payer.getCurrency());
        
        transDAO.insert(transaction);
    }
    
    @Override
    public int getNumRequests(SystemUser user) {
        return transDAO.getPaymentRequests(user).size();
    }
    
    @Override
    public List<PaymentTransaction> getPaymentRequests(SystemUser user) {
        return transDAO.getPaymentRequests(user);
    }
    
    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void makePayment(String payerUsername, String payeeUsername, float amount, String description) {
        if(payerUsername.equals(payeeUsername)) throw new EJBException("User cannot make a payment to self.");
        
        SystemUser payer = usrSrv.getUser(payerUsername);
        // Exception thrown if no user is found with payeeUsername.
        SystemUser payee;
        if(usrSrv.hasUser(payeeUsername)) {
            payee = usrSrv.getUser(payeeUsername);
        } else {
            throw new EJBException("User does not exist.");
        }
        
        float transferAmount = convert(payer.getCurrency(), payee.getCurrency(), amount);
        PaymentTransaction transaction = new PaymentTransaction(
                payer, payee, transferAmount, description, payer.getCurrency());

        if(payer.getBalance() >= transferAmount) {

            payer.setBalance(payer.getBalance() - amount);
            payee.setBalance(payee.getBalance() + transferAmount);

            transaction.setStatus("COMPLETE");

            transDAO.insert(transaction);
        } else {
            throw new EJBException("Insufficient funds.");
        }
    }
    
    private float convert(String currency1, String currency2, float amount) {
        return convMan.getConvertedAmount(currency1, currency2, amount);
    }
}
