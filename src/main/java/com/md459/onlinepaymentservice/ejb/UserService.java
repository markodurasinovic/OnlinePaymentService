/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.md459.onlinepaymentservice.ejb;

import com.md459.onlinepaymentservice.entity.User;
import com.md459.onlinepaymentservice.entity.UserGroup;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.*;

/**
 *
 * @author marko
 */
@Stateless
public class UserService {
    
    @PersistenceContext
    EntityManager em;
    
    public UserService() {}
    
    public void registerUser(String username, String password, String name, String surname) {
        em.persist(new User(username, getDigest(password), name, surname));
        em.persist(new UserGroup(username, "users"));
    }
    
    public void registerAdmin(String username, String password, String name, String surname) {
        em.persist(new User(username, getDigest(password), name, surname));
        em.persist(new UserGroup(username, "admins"));
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
            Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return pwd;
    }
}
