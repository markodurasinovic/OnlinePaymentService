package com.md459.onlinepaymentservice.dto;

import java.io.Serializable;
import java.util.List;

/**
 * A DTO for SystemUserGroup entities.
 * 
 * Provides getter methods to allow field access in JSF.
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
