/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.md459.onlinepaymentservice.ejb;

import com.md459.onlinepaymentservice.dao.PaymentTransactionDAO;
import com.md459.onlinepaymentservice.dao.SystemUserDAO;
import com.md459.onlinepaymentservice.dto.PaymentTransactionTO;
import com.md459.onlinepaymentservice.dto.SystemUserTO;
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
    SystemUserDAO usrDAO;
    
    @EJB
    UserService usrSrv;
    
    @EJB
    ConversionManager convMan;
    
    public PaymentTransactionServiceBean() {}
    
    @Override
    public List<PaymentTransactionTO> getAllTransactions() {
        return transDAO.getAll();
    }
    
    @Override
    public List<PaymentTransactionTO> getTransactionHistory(SystemUserTO user) {
        return transDAO.getHistory(user);
    }
    
    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void acceptRequest(long reqId) {
        PaymentTransactionTO transaction = getTransaction(reqId);
        
        if(transaction.status.equals("PENDING")) {
            SystemUserTO requestee = transaction.payer;
            SystemUserTO requester = transaction.payee;

            float transferAmount = convert(requester.currency, requestee.currency, transaction.amount);
            if(requestee.balance >= transferAmount) {
                requestee.balance -= transferAmount;
                requester.balance += transaction.amount;

                transaction.status = "COMPLETE";
                
                usrDAO.update(requestee);
                usrDAO.update(requester);
                transDAO.update(transaction);
            } else {
                throw new EJBException("Insufficient funds.");
            }
        }
    }
    
    @Override
    public void rejectRequest(long reqId) {
        PaymentTransactionTO transaction = getTransaction(reqId);
        
        if(transaction.status.equals("PENDING")) {
            transaction.status = "VOID";            
            transDAO.update(transaction);
        }        
    }
    
    private PaymentTransactionTO getTransaction(long id) {
        return transDAO.getById(id);
    }
    
    @Override
    public void requestPayment(String requester, String requestee, float amount, String description) {
        if(requester.equals(requestee)) throw new EJBException("User cannot request a payment from self.");
        
        SystemUserTO payee = usrSrv.getUser(requester);
        // Exception thrown if no user is found with username requestee.
        SystemUserTO payer;
        if(usrSrv.hasUser(requestee)) {
            payer = usrSrv.getUser(requestee);
        } else {
            throw new EJBException("User does not exist.");
        }
        
        float transferAmount = convert(payer.currency, payee.currency, amount);
        PaymentTransactionTO transaction = new PaymentTransactionTO(
                payer, payee, transferAmount, description, payer.currency);
        
        transDAO.insert(transaction);
    }
    
    @Override
    public int getNumRequests(SystemUserTO user) {
        return transDAO.getPaymentRequests(user).size();
    }
    
    @Override
    public List<PaymentTransactionTO> getPaymentRequests(SystemUserTO user) {
        return transDAO.getPaymentRequests(user);
    }
    
    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void makePayment(String payerUsername, String payeeUsername, float amount, String description) {
        if(payerUsername.equals(payeeUsername)) throw new EJBException("User cannot make a payment to self.");
        
        SystemUserTO payer = usrSrv.getUser(payerUsername);
        // Exception thrown if no user is found with payeeUsername.
        SystemUserTO payee;
        if(usrSrv.hasUser(payeeUsername)) {
            payee = usrSrv.getUser(payeeUsername);
        } else {
            throw new EJBException("User does not exist.");
        }
        
        float transferAmount = convert(payer.currency, payee.currency, amount);
        PaymentTransactionTO transaction = new PaymentTransactionTO(
                payer, payee, transferAmount, description, payer.currency);

        if(payer.balance >= transferAmount) {

            payer.balance -= amount;
            payee.balance += transferAmount;

            transaction.status = "COMPLETE";

            usrDAO.update(payer);
            usrDAO.update(payee);
            transDAO.insert(transaction);
        } else {
            throw new EJBException("Insufficient funds.");
        }
    }
    
    private float convert(String currency1, String currency2, float amount) {
        return convMan.getConvertedAmount(currency1, currency2, amount);
    }
}
