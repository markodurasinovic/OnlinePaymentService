/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.md459.onlinepaymentservice.dto;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author marko
 */
public class SystemUserGroupTO implements Serializable {
    public Long id;
    public String groupname;
    public List<SystemUserTO> users;
    public String username;
    
    public SystemUserGroupTO() {}
    
    // Getters so that DTO's fields can be used in JSF

    public Long getId() {
        return id;
    }

    public String getGroupname() {
        return groupname;
    }

    public List<SystemUserTO> getUsers() {
        return users;
    }

    public String getUsername() {
        return username;
    }
}
