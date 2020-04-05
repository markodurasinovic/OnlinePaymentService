/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.md459.onlinepaymentservice.ejb;

import com.md459.onlinepaymentservice.entity.PaymentTransaction;
import com.md459.onlinepaymentservice.entity.SystemUser;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author marko
 */
@Local
public interface PaymentTransactionService {
    
    public List<PaymentTransaction> getAllTransactions();
    public void acceptRequest(long reqId);
    public void rejectRequest(long reqId);
    public List<PaymentTransaction> getTransactionHistory(SystemUser user);
    public void requestPayment(String username, double amount, String description);
    public int getNumRequests(SystemUser user);
    public List<PaymentTransaction> getPaymentRequests(SystemUser user);
    public void makePayment(String username, double amount, String description);
}
