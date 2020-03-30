/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.md459.onlinepaymentservice.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 *
 * @author marko
 */
@Entity
public class SystemUserGroup implements Serializable {
    
    @Id
    @GeneratedValue
    private Long id;
    
    @NotNull
    private String groupname;
    
    @OneToMany(mappedBy = "usergroup", cascade=CascadeType.PERSIST)
    @JoinTable(name = "systemusergroup_systemuser",
            joinColumns={@JoinColumn(name = "groupname", referencedColumnName = "groupname")},
            inverseJoinColumns={@JoinColumn(name = "username", referencedColumnName = "username")}
    )
    private List<SystemUser> users;
    
    public SystemUserGroup() {}
    
    public SystemUserGroup(String groupname) {
        this.groupname = groupname;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 37 * hash + Objects.hashCode(this.id);
        hash = 37 * hash + Objects.hashCode(this.groupname);
        hash = 37 * hash + Objects.hashCode(this.users);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SystemUserGroup other = (SystemUserGroup) obj;
        if (!Objects.equals(this.users, other.users)) {
            return false;
        }
        if (!Objects.equals(this.groupname, other.groupname)) {
            return false;
        }
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGroupname() {
        return groupname;
    }

    public void setGroupname(String groupname) {
        this.groupname = groupname;
    }

    public List<SystemUser> getUsers() {
        return users;
    }

    public void setUsers(List<SystemUser> users) {
        this.users = users;
    }
    
    public void addUser(SystemUser user) {
        if(users == null) {
            users = new ArrayList<>();
        }
        user.setUsergroup(this);
        users.add(user);
    }
}
