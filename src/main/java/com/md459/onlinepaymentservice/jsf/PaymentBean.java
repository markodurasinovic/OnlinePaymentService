/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.md459.onlinepaymentservice.jsf;

import com.md459.onlinepaymentservice.ejb.PaymentTransactionService;
import com.md459.onlinepaymentservice.entity.SystemUser;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
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
    
    private SystemUser payee;
    private float amount;
    private String description;
    
    public PaymentBean() {}
    
    public String requestPayment(String requestee) {
        FacesContext fc = FacesContext.getCurrentInstance();
        String requester = fc.getExternalContext().getRemoteUser();
        txnSrv.requestPayment(requester, requestee, amount, description);
        // add a payment confirmation page
        return "user";
    }
    
    public String makePayment(String payee) {
        FacesContext fc = FacesContext.getCurrentInstance();
        String payer = fc.getExternalContext().getRemoteUser();
        txnSrv.makePayment(payer, payee, amount, description);
        // add a payment confirmation page
        return "user";
    }

    public SystemUser getPayee() {
        return payee;
    }

    public void setPayee(SystemUser payee) {
        this.payee = payee;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    
}
