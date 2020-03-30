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
import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author marko
 */
@Startup
@Singleton
public class UserGroupService {
    
    @PersistenceContext
    private EntityManager em;
    
    private SystemUserGroup adminGroup;
    private SystemUserGroup userGroup;
    
    public UserGroupService() {}
    
    @PostConstruct
    public void init() {
        adminGroup = new SystemUserGroup("ADMIN");
        userGroup = new SystemUserGroup("USER");
        
        em.persist(adminGroup);
        em.persist(userGroup);
        
        registerAdmin("admin1", "admin1", null, null);
    }
    
    public void registerUser(String username, String password, String name, String surname) {
        SystemUser user = new SystemUser(username, getDigest(password), name, surname);
        userGroup.addUser(user);
        em.persist(user);        
    }
    
    public void registerAdmin(String username, String password, String name, String surname) {
        SystemUser admin = new SystemUser(username, getDigest(password), name, surname);
        adminGroup.addUser(admin);
        em.persist(admin);
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

    public SystemUserGroup getAdminGroup() {
        return adminGroup;
    }

    public SystemUserGroup getUserGroup() {
        return userGroup;
    }
}
