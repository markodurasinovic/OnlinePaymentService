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
public class AdminBean implements Serializable {
    
    @EJB
    UserService usrSrv;
    
    @EJB
    PaymentTransactionService txnSrv;
    
    private SystemUserTO observedUser;
    
    public AdminBean() {}
    
    public List<PaymentTransactionTO> getTransactionHistory(SystemUserTO user) {
        return txnSrv.getTransactionHistory(user);
    }
    
    public List<PaymentTransactionTO> getAllTransactions() {
        return txnSrv.getAllTransactions();
    }
    
    public List<SystemUserTO> getAllUsers() {
        return usrSrv.getAllUsers();
    }

    public SystemUserTO getObservedUser() {
        FacesContext fc = FacesContext.getCurrentInstance();
        Map<String, String> params = fc.getExternalContext().getRequestParameterMap();
        String username = params.get("username");
        observedUser = (username != null) ? usrSrv.getUser(username) : observedUser;
        
        return observedUser;
    }

    public void setObservedUser(SystemUserTO observedUser) {
        this.observedUser = observedUser;
    }
}
