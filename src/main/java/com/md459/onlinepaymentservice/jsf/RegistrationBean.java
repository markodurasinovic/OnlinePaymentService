package com.md459.onlinepaymentservice.jsf;


import com.md459.onlinepaymentservice.ejb.UserService;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author marko
 */
@Named
@RequestScoped
public class RegistrationBean {
    
    @EJB
    UserService usrSrv;

    private String username;
    private String password;
    private String name;
    private String surname;
    
    public RegistrationBean() {
        
    }
    
    public String registerUser() {
        usrSrv.registerUser(username, password, name, surname);
        return "index";
    }
    
    public String registerAdmin() {
        usrSrv.registerAdmin(username, password, name, surname);
        return "index";
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }
    
}
