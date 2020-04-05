/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.md459.onlinepaymentservice.jsf;

import com.md459.onlinepaymentservice.ejb.PaymentTransactionService;
import com.md459.onlinepaymentservice.ejb.UserService;
import com.md459.onlinepaymentservice.entity.SystemUser;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

/**
 *
 * @author marko
 */
@Named
@RequestScoped
public class PaymentBean {
        
    @EJB
    PaymentTransactionService txnSrv;
    
    private SystemUser toUser;
    private double amount;
    private String description;
    
    public PaymentBean() {}
    
    public String requestPayment(String username) {
        txnSrv.requestPayment(username, amount, description);
        // add a payment confirmation page
        return "user";
    }
    
    public String makePayment(String username) {
        txnSrv.makePayment(username, amount, description);
        // add a payment confirmation page
        return "user";
    }

    public SystemUser getToUser() {
        return toUser;
    }

    public void setToUser(SystemUser toUser) {
        this.toUser = toUser;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    
}
