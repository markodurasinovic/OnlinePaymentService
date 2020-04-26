/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.md459.onlinepaymentservice.ejb;

import com.md459.onlinepaymentservice.dao.SystemUserDAO;
import com.md459.onlinepaymentservice.entity.SystemUser;
import com.md459.onlinepaymentservice.entity.SystemUserGroup;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

/**
 *
 * @author marko
 */
@Stateless
public class UserServiceBean implements UserService {
    
    @EJB
    SystemUserDAO userDAO;
    
    @EJB
    ConversionManager convMan;
    
    public UserServiceBean() {}
    
    @Override
    public List<SystemUser> getAllUsers() {
        return userDAO.getAllUsers();
    }
    
    @Override
    public SystemUser getUser(String username) {
        return userDAO.getByUsername(username);
    }
    
    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void registerUser(String username, String password, String name, String surname, String currency) {
         if(hasUser(username))
             throw new EJBException("User with this username already exists.");
         
        register(username, password, name, surname, currency); 
    }
    
    @Override
    public boolean hasUser(String username) {
        return userDAO.getByUsername(username) != null;            
    }
    
    private void register(String username, String password, String name, String surname, String currency) {
        SystemUser user = new SystemUser(username, getDigest(password), name, surname);
        SystemUserGroup group = new SystemUserGroup(username, "USER");
        
        float initialBalance = getInitialBalance(currency);
        user.setBalanceAndCurrency(initialBalance, currency);
        
        userDAO.insert(user, group);
    }
    
    private float getInitialBalance(String currency) {
        return convMan.getConvertedAmount("GBP", currency, 1000.00f);
    }
    
    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void registerAdmin(String username, String password) {
        if(hasUser(username)) 
            throw new EJBException("Admin with this username already exists.");
        
        SystemUser admin = new SystemUser(username, getDigest(password));
        SystemUserGroup group = new SystemUserGroup(username, "ADMIN");
        
        userDAO.insert(admin, group);
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
