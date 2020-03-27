/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.md459.onlinepaymentservice.ejb;

import com.md459.onlinepaymentservice.entity.SystemUser;
import com.md459.onlinepaymentservice.entity.SystemUserGroup;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.*;
import javax.persistence.*;

/**
 *
 * @author marko
 */
@Stateless
public class UserService {
    
    @PersistenceContext
    private EntityManager em;
    
    @EJB
    UserGroupService ugs;
    
    public UserService() {}
        
    public void registerUser(String username, String password, String name, String surname) {
        SystemUser user = new SystemUser(username, getDigest(password), name, surname);
        SystemUserGroup users = ugs.getUserGroup();
        users.addUser(user);
        em.persist(user);        
    }
    
    public void registerAdmin(String username, String password, String name, String surname) {
        SystemUser user = new SystemUser(username, getDigest(password), name, surname);
        SystemUserGroup admins = ugs.getAdminGroup();
        admins.addUser(user);
        em.persist(user);
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
