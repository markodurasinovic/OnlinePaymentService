/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.md459.onlinepaymentservice.jsf;

import com.md459.onlinepaymentservice.ejb.UserService;
import com.md459.onlinepaymentservice.entity.SystemUser;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.bean.ViewScoped;
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
    UserService usrSrv;
    
    private SystemUser toUser;
    private double amount;
    
    public PaymentBean() {}
    
    public String makePayment(String username) {
        usrSrv.makePayment(username, amount);
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
    
    
}
