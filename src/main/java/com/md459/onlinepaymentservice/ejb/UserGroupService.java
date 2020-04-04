/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.md459.onlinepaymentservice.ejb;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;

/**
 *
 * @author marko
 */
@Startup
@Singleton
public class UserGroupService {
    
    @EJB
    UserService usrSrv;
    
    public UserGroupService() {}
    
    @PostConstruct
    public void init() {
        usrSrv.registerAdmin("admin1", "admin1");
    }
}
