/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.md459.onlinepaymentservice.ejb;

import com.md459.onlinepaymentservice.dao.SystemUserDAO;
import com.md459.onlinepaymentservice.dto.SystemUserTO;
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
    public List<SystemUserTO> getAllUsers() {
        return userDAO.getAllUsers();
    }
    
    @Override
    public SystemUserTO getUser(String username) {
        return userDAO.getByUsername(username);
    }
    
    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void registerUser(String username, String password, String name, String surname, String currency) {
         if(hasUser(username))
             throw new EJBException("User with this username already exists.");
         
        float initialBalance = getInitialBalance(currency);
        SystemUserTO user = new SystemUserTO(
                username, getDigest(password), name, surname, initialBalance, currency);
        
        userDAO.insert(user, "USER");
    }
    
    private float getInitialBalance(String currency) {
        return convMan.getConvertedAmount("GBP", currency, 1000.00f);
    }
    
    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void registerAdmin(String username, String password) {
        if(hasUser(username)) 
            throw new EJBException("Admin with this username already exists.");
        
        SystemUserTO admin = new SystemUserTO(username, getDigest(password));
        
        userDAO.insert(admin, "ADMIN");
    }
    
    @Override
    public boolean hasUser(String username) {
        return userDAO.getByUsername(username) != null;            
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
