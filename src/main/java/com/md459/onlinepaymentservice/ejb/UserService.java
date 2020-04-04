/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.md459.onlinepaymentservice.ejb;

import com.md459.onlinepaymentservice.entity.PaymentTransaction;
import com.md459.onlinepaymentservice.entity.SystemUser;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author marko
 */
@Local
public interface UserService {
    
    public void requestPayment(String username, double amount, String description);
    public int getNumRequests(SystemUser user);
    public List<PaymentTransaction> getPaymentRequests(SystemUser user);
    public void makePayment(String username, double amount, String description);
    public SystemUser getCurrentUser();
    public SystemUser getUser(String username);
    public List<SystemUser> searchUsers(String searchTerm);
    public void registerUser(String username, String password, String name, String surname, String currency);
    public void registerAdmin(String username, String password);
}
