package com.md459.onlinepaymentservice.jsf;


import com.md459.onlinepaymentservice.ejb.UserService;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;

/**
 * RegistrationBean provides functionality for registering users and admins.
 * 
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
    private String currency;
    
    public RegistrationBean() {
        
    }
    
    public String registerUser() {
        FacesContext fc = FacesContext.getCurrentInstance();
        try {
            usrSrv.registerUser(username, password, name, surname, currency);
            return "index";
        } catch(EJBException e) {
            fc.addMessage(null, new FacesMessage(e.getMessage()));
            return "registerUser";
        }        
    }
    
    public String registerAdmin() {
        FacesContext fc = FacesContext.getCurrentInstance();
        try {
            usrSrv.registerAdmin(username, password);
            return "admin";
        } catch(EJBException e) {
            fc.addMessage(null, new FacesMessage(e.getMessage()));
            return "registerAdmin";
        }
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
    
    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
