/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.md459.onlinepaymentservice.jsf;

import com.md459.onlinepaymentservice.ejb.UserService;
import com.md459.onlinepaymentservice.entity.SystemUser;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

/**
 *
 * @author marko
 */
@Named
@RequestScoped
public class SearchBean {
    
    @EJB
    UserService usrSrv;
    
    private String searchTerm;
    
    public List<SystemUser> searchUsers() {
        return usrSrv.searchUsers(searchTerm);
    }

    public String getSearchTerm() {
        return searchTerm;
    }

    public void setSearchTerm(String searchTerm) {
        this.searchTerm = searchTerm;
    }
}
