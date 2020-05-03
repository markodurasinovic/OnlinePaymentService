package com.md459.onlinepaymentservice.jsf;

import com.md459.onlinepaymentservice.dto.PaymentTransactionTO;
import com.md459.onlinepaymentservice.dto.SystemUserTO;
import com.md459.onlinepaymentservice.ejb.PaymentTransactionService;
import com.md459.onlinepaymentservice.ejb.UserService;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;

/**
 * AdminBean provides functionality unique to ADMIN SystemUsers.
 */
@Named
@RequestScoped
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

    /**
     * Get the username for the currently selected user and set observedUser.
     * This is used when viewing a particular user's payment history.
     * 
     * @return - A SystemUser DTO.
     */
    public SystemUserTO getObservedUser() {
        FacesContext fc = FacesContext.getCurrentInstance();
        Map<String, String> params = fc.getExternalContext().getRequestParameterMap();
        String username = params.get("username");
        observedUser = (username != null) ? usrSrv.getUser(username) : observedUser;
        
        return observedUser;
    }

}
