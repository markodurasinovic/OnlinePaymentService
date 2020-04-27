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
import com.md459.onlinepaymentservice.entity.PaymentTransaction;
import com.md459.onlinepaymentservice.entity.SystemUser;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
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
    private SystemUserTO toUser;
    
    /**
     * TODO: REDUCE NUMBER OF QUERIES TO USER TABLE
     * right now, one query is being made for each field displayed
     * in view (name, surname, currency, balance).
     * instead, on view-display, one query should run - retrieving
     * all necessary information.
     */
    public UserBean() {}
    
    @PostConstruct
    public void init() {
        user = getUser();
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
        FacesContext fc = FacesContext.getCurrentInstance();
        String username = fc.getExternalContext().getRemoteUser();
                
        if(username != null) user =  usrSrv.getUser(username);
                
        return user;
    }
    
    public SystemUserTO getToUser() {
        FacesContext fc = FacesContext.getCurrentInstance();
        Map<String, String> params = fc.getExternalContext().getRequestParameterMap();
        
        String username = params.get("toUser");
        // if toUser f:param has been set, indicates that toUser for the
        // session has changed. i.e. the current user has selected a new 
        // user to pay/request a payment from
        toUser = (username != null) ? usrSrv.getUser(username) : toUser;
        
        return toUser;
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
        return user.balance;
    }
}
