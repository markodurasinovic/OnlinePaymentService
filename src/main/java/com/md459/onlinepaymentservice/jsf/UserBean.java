/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.md459.onlinepaymentservice.jsf;

import com.md459.onlinepaymentservice.ejb.PaymentTransactionService;
import com.md459.onlinepaymentservice.ejb.UserService;
import com.md459.onlinepaymentservice.entity.PaymentTransaction;
import com.md459.onlinepaymentservice.entity.SystemUser;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;

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
    
    private SystemUser user;
    private SystemUser toUser;
    
    public UserBean() {}
    
    public List<PaymentTransaction> getTransactionHistory() {
        return txnSrv.getTransactionHistory(user);
    }
    
    public void acceptRequest(long reqId) {
        txnSrv.acceptRequest(reqId);
    }
    
    public void rejectRequest(long reqId) {
        txnSrv.rejectRequest(reqId);
    }
    
    public List<PaymentTransaction> getPaymentRequests() {
        return txnSrv.getPaymentRequests(user);
    }
    
    public int getNumRequests() {
        return txnSrv.getNumRequests(user);
    }

    public SystemUser getUser() {
        user = (user == null) ? usrSrv.getCurrentUser() : user;
        
        return user;
    }
    
    public SystemUser getToUser() {
        FacesContext fc = FacesContext.getCurrentInstance();
        Map<String, String> params = fc.getExternalContext().getRequestParameterMap();
        
        String username = params.get("toUser");
        toUser = (username != null) ? usrSrv.getUser(username) : toUser;
        
        return toUser;
    }
}
