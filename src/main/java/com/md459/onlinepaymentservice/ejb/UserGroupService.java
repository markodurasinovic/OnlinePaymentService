/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.md459.onlinepaymentservice.ejb;

import com.md459.onlinepaymentservice.entity.SystemUser;
import com.md459.onlinepaymentservice.entity.SystemUserGroup;
import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.persistence.*;

/**
 *
 * @author marko
 */
@Startup
@Singleton
public class UserGroupService {
    
    @PersistenceContext
    private EntityManager em;
    
    private SystemUserGroup userGroup;
    private SystemUserGroup adminGroup;
    
    public UserGroupService() {}
    
    @PostConstruct
    public void init() {
        userGroup = new SystemUserGroup("users");
        adminGroup = new SystemUserGroup("admins");
        
        SystemUser admin = new SystemUser("admin1", "admin1", null, null);
        adminGroup.addUser(admin);
        
        em.persist(userGroup);
        em.persist(adminGroup); 
    }

    public SystemUserGroup getUserGroup() {
        return userGroup;
    }

    public SystemUserGroup getAdminGroup() {
        return adminGroup;
    }
    
}
