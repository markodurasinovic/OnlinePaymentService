/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.md459.onlinepaymentservice.jsf;

import com.md459.onlinepaymentservice.dto.PaymentTransactionTO;
import com.md459.onlinepaymentservice.dto.SystemUserTO;
import com.md459.onlinepaymentservice.ejb.PaymentTransactionService;
import com.md459.onlinepaymentservice.ejb.UserService;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author marko
 */
@Named
@SessionScoped
public class UserBean implements Serializable {
    
    @EJB
    UserService usrSrv;
    
    @EJB
    PaymentTransactionService txnSrv;
    
    private SystemUserTO user;
    
    public UserBean() {}
    
    @PostConstruct
    public void init() {
        updateUser();
    }
    
    public String logout() {
        FacesContext fc = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) fc.getExternalContext().getRequest();
        try {
            request.logout();
            request.getSession().invalidate();
            fc.addMessage(null, new FacesMessage("User is logged out."));
            return "index";
        } catch (ServletException e) {
            fc.addMessage(null, new FacesMessage("Logout failed."));
            return "user";
        }
    }
    
    public List<PaymentTransactionTO> getTransactionHistory() {
        return txnSrv.getTransactionHistory(user);
    }
    
    public List<PaymentTransactionTO> getPaymentRequests() {
        return txnSrv.getPaymentRequests(user);
    }
    
    public int getNumRequests() {
        return txnSrv.getNumRequests(user);
    }

    public SystemUserTO getUser() {
        return user;
    }
    
    public String getName() {
        return user.name;
    }
    
    public String getSurname() {
        return user.surname;
    }
    
    public String getCurrency() {
        return user.currency;
    }
    
    public float getBalance() {
        // Always display the updated user balance
        updateUser();        
        return user.balance;
    }
    
    private void updateUser() {
        FacesContext fc = FacesContext.getCurrentInstance();
        String username = fc.getExternalContext().getRemoteUser();
        
        user = usrSrv.getUser(username);
    }
}
