/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.md459.onlinepaymentservice.jsf;

import com.md459.onlinepaymentservice.ejb.PaymentTransactionService;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
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
    
    private String username;
    private float amount;
    private String description;
    
    public PaymentBean() {}
    
    public String requestPayment() {
        FacesContext fc = FacesContext.getCurrentInstance();
        String requester = fc.getExternalContext().getRemoteUser();
        try {
            txnSrv.requestPayment(requester, username, amount, description);
            return "success";
        } catch(EJBException e) {
            fc.addMessage(null, new FacesMessage(e.getMessage()));
            return "fail";
        }
    }
    
    public String makePayment() {
        FacesContext fc = FacesContext.getCurrentInstance();
        String payer = fc.getExternalContext().getRemoteUser();
        try {
            txnSrv.makePayment(payer, username, amount, description);
            return "success";
        } catch(EJBException e) {
            fc.addMessage(null, new FacesMessage(e.getMessage()));
            return "fail";
        }
    }
    
    public void acceptRequest(long reqId) {
        FacesContext fc = FacesContext.getCurrentInstance();
        try {
            txnSrv.acceptRequest(reqId);
            fc.addMessage(null, new FacesMessage("Request has been accepted."));
        } catch(EJBException e) {
            fc.addMessage(null, new FacesMessage(e.getMessage()));
        }
    }
    
    public void rejectRequest(long reqId) {
        FacesContext fc = FacesContext.getCurrentInstance();
        txnSrv.rejectRequest(reqId);
        fc.addMessage(null, new FacesMessage("Request has been rejected."));
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    
    
}
