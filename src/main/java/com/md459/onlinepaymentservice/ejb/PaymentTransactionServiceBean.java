package com.md459.onlinepaymentservice.ejb;

import com.md459.onlinepaymentservice.clients.ConversionClient;
import com.md459.onlinepaymentservice.clients.TimestampClient;
import com.md459.onlinepaymentservice.dao.PaymentTransactionDAO;
import com.md459.onlinepaymentservice.dao.SystemUserDAO;
import com.md459.onlinepaymentservice.dto.PaymentTransactionTO;
import com.md459.onlinepaymentservice.dto.SystemUserTO;
import java.util.List;
import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

/**
 * A Stateless EJB implementation of the PaymentTransactionService interface.
 * It provides functionality regarding PaymentTransactions, including making,
 * and retrieving, payments and requests.
 */
@Stateless
@DeclareRoles({"USER", "ADMIN"})
public class PaymentTransactionServiceBean implements PaymentTransactionService {
        
    @EJB
    PaymentTransactionDAO transDAO;
    
    @EJB
    SystemUserDAO usrDAO;
    
    @EJB
    UserService usrSrv;
    
    @EJB
    ConversionClient conversion;
    
    @EJB
    TimestampClient timestamp;
    
    public PaymentTransactionServiceBean() {}
    
    /**
     * Get all PaymentTransactions.
     * 
     * @return - A list of PaymentTransaction DTOs.
     */
    @Override
    @RolesAllowed("ADMIN")
    public List<PaymentTransactionTO> getAllTransactions() {
        return transDAO.getAll();
    }
    
    /**
     * Get the PaymentTransaction history for user.
     * 
     * @param user - A SystemUser DTO.
     * @return - A list of PaymentTransaction DTOs.
     */
    @Override
    @RolesAllowed({"USER", "ADMIN"})
    public List<PaymentTransactionTO> getTransactionHistory(SystemUserTO user) {
        return transDAO.getHistory(user);
    }
    
    /**
     * Complete the PENDING PaymentTransaction (request) with reqId.
     * 
     * @param reqId - A PaymentTransaction id.
     */
    @Override
    @RolesAllowed("USER")
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
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
    
    /**
     * Make the PENDING PaymentTransaction (request) with id reqId VOID.
     * 
     * @param reqId - A PaymentTransaction id.
     */
    @Override
    @RolesAllowed("USER")
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
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
    
    /**
     * Create a new PENDING PaymentTransaction (request).
     * 
     * @param requester - Username of the user to be paid.
     * @param requestee - Username of the user to pay.
     * @param amount - Amount to be transferred, in requester's currency.
     * @param description - An optional description for the PaymentTransaction.
     */
    @Override
    @RolesAllowed("USER")
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void requestPayment(String requester, String requestee, float amount, String description) {
        if(requester.equals(requestee)) throw new EJBException("User cannot request a payment from self.");
        if(amount <= 0f) throw new EJBException("You must request more than 0.");
        
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
        try {
            transaction.creationTime = timestamp.getTimestamp();
        } catch(Exception e) {
            System.err.println(e);
            throw new EJBException("Timestamp service failed.");
        }
        
        transDAO.insert(transaction);
    }
    
    /**
     * Get the number of requests made to user.
     * 
     * @param user - A SystemUser DTO.
     * @return - A list of PaymentTransaction DTOs.
     */
    @Override
    @RolesAllowed("USER")
    public int getNumRequests(SystemUserTO user) {
        return transDAO.getPaymentRequests(user).size();
    }
    
    /**
     * Get all PENDING requests to user.
     * 
     * @param user - A SystemUser DTO.
     * @return - A list of PaymentTransaction DTOs.
     */
    @Override
    @RolesAllowed("USER")
    public List<PaymentTransactionTO> getPaymentRequests(SystemUserTO user) {
        return transDAO.getPaymentRequests(user);
    }
    
    /**
     * Create a new PaymentTransaction, transferring money from payer to payee.
     * 
     * @param payerUsername - Username of Payer.
     * @param payeeUsername - Username of Payee.
     * @param amount - Amount to be transferred, in Payer's currency.
     * @param description - An optional description of PaymentTransaction.
     */
    @Override
    @RolesAllowed("USER")
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void makePayment(String payerUsername, String payeeUsername, float amount, String description) {
        if(payerUsername.equals(payeeUsername)) throw new EJBException("User cannot make a payment to self.");
        if(amount <= 0f) throw new EJBException("You must transfer more than 0.");
        
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
            
            try {
                transaction.creationTime = timestamp.getTimestamp();
            } catch(Exception e) {
                throw new EJBException("Timestamp service failed.");
            }
            
            usrDAO.update(payer);
            usrDAO.update(payee);
            transDAO.insert(transaction);
        } else {
            throw new EJBException("Insufficient funds.");
        }
    }
    
    private float convert(String currency1, String currency2, float amount) {
        return conversion.getConvertedAmount(currency1, currency2, amount);
    }
}
