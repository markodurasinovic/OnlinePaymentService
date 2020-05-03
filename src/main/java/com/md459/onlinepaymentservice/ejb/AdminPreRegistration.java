package com.md459.onlinepaymentservice.ejb;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;

/**
 * A Startup Singleton EJB which registers an ADMIN SystemUser on startup.
 * username: admin1
 * password: admin1
 */
@Startup
@Singleton
public class AdminPreRegistration {
    
    @EJB
    UserService usrSrv;
    
    public AdminPreRegistration() {}
    
    @PostConstruct
    public void init() {
        usrSrv.registerAdmin("admin1", "admin1");
    }
}
