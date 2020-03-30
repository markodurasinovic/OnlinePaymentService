/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.md459.onlinepaymentservice.ejb;

import javax.ejb.*;

/**
 *
 * @author marko
 */
@Stateless
public class UserService {
    
    @EJB
    private UserGroupService ugs;
    
    public UserService() {}
        
    public void registerUser(String username, String password, String name, String surname) {
        ugs.registerUser(username, password, name, surname);
    }
    
    public void registerAdmin(String username, String password, String name, String surname) {
        ugs.registerAdmin(username, password, name, surname);
    }
}
