/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.md459.onlinepaymentservice.jsf;

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
    
    private SystemUser user;
    private SystemUser toUser;
    
    public UserBean() {}
    
    public List<PaymentTransaction> getTransactionHistory() {
        return usrSrv.getTransactionHistory(user);
    }
    
    public void acceptRequest(long reqId) {
        usrSrv.acceptRequest(reqId);
    }
    
    public void rejectRequest(long reqId) {
        usrSrv.rejectRequest(reqId);
    }
    
    public List<PaymentTransaction> getPaymentRequests() {
        return usrSrv.getPaymentRequests(user);
    }
    
    public int getNumRequests() {
        return usrSrv.getNumRequests(user);
    }

    public SystemUser getUser() {
        user = usrSrv.getCurrentUser();
        
        return user;
    }
    
    public SystemUser getToUser() {
        FacesContext fc = FacesContext.getCurrentInstance();
        Map<String, String> params = fc.getExternalContext().getRequestParameterMap();
        
        String username = params.get("toUser");
        if(username != null) {
            toUser = usrSrv.getUser(username);
        }
        
        return toUser;
    }
}
