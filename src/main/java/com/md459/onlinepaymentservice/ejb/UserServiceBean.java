/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.md459.onlinepaymentservice.ejb;

import com.md459.onlinepaymentservice.entity.PaymentTransaction;
import com.md459.onlinepaymentservice.entity.SystemUser;
import com.md459.onlinepaymentservice.entity.SystemUserGroup;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.*;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

/**
 *
 * @author marko
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class UserServiceBean implements UserService {
    
    @PersistenceContext
    private EntityManager em;
    
    public UserServiceBean() {}
    
    @Override
    public void makePayment(String username, double amount) {
        SystemUser fromUser = getCurrentUser();
        SystemUser toUser = getUser(username);
        
        System.out.println("Transfering from " + fromUser + " to " + toUser);
        
        double transferAmount = convert(amount, fromUser.getCurrency(), toUser.getCurrency());
        PaymentTransaction transaction = new PaymentTransaction(fromUser, toUser, transferAmount);
        
        fromUser.setBalance(fromUser.getBalance() - amount);
        toUser.setBalance(toUser.getBalance() + transferAmount);
        
        em.persist(transaction);
    }
    
    private double convert(double amount, String currencyFrom, String currencyTo) {
        // TODO: implement conversion
        return amount;
    }
    
    @Override
    public SystemUser getCurrentUser() {
        String username = FacesContext.getCurrentInstance().getExternalContext().getRemoteUser();
        System.out.print("GETTING CURRENT USER " + username);
        return getUser(username);
    }
    
    @Override
    public SystemUser getUser(String username) {
        TypedQuery<SystemUser> query = em.createQuery(
                "SELECT u FROM SystemUser u WHERE u.username = :username", SystemUser.class);
                
        return query
                .setParameter("username", username)
                .getSingleResult();
    }
    
    @Override
    public List<SystemUser> searchUsers(String searchTerm) {
        TypedQuery<SystemUser> query = em.createQuery(
                "SELECT u FROM SystemUser u WHERE CONCAT(u.name, \" \", u.surname) LIKE :searchTerm", SystemUser.class);
        
        String pattern = "%" + searchTerm.toLowerCase() + "%";
        return query
                .setParameter("searchTerm", pattern)
                .getResultList();
    }
    
    @Override
    public void registerUser(String username, String password, String name, String surname, String currency) {
        SystemUser user = new SystemUser(username, getDigest(password), name, surname, currency);
        SystemUserGroup group = new SystemUserGroup(username, "USER");
        
        group.addUser(user);
        em.persist(group);     
    }
    
    @Override
    public void registerAdmin(String username, String password) {
        SystemUser admin = new SystemUser(username, getDigest(password));
        SystemUserGroup group = new SystemUserGroup(username, "ADMIN");
        
        group.addUser(admin);
        em.persist(group);
    }
    
    private String getDigest(String password) {
        String pwd = password;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(password.getBytes("UTF-8"));
            byte[] digest = md.digest();
            StringBuffer sb = new StringBuffer();
            for(int i = 0; i < digest.length; i++) {
                sb.append(Integer.toString((digest[i] & 0xff) + 0x100, 16).substring(1));
            }
            
            pwd = sb.toString();
        } catch(UnsupportedEncodingException | NoSuchAlgorithmException ex) {
            Logger.getLogger(UserServiceBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return pwd;
    }
}
