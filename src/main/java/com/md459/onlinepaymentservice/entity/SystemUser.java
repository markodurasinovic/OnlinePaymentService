/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.md459.onlinepaymentservice.entity;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 *
 * @author marko
 */
@Entity
public class SystemUser implements Serializable {

    @Id
    @GeneratedValue
    private Long id;
    
    @NotNull
    @Column(unique = true)
    private String username;
    
    @NotNull
    private String userpassword;
    
    private String name;
    private String surname;
    
    @ManyToOne
    @JoinTable(name="systemusergroup_systemuser",
            joinColumns={@JoinColumn(name = "username", referencedColumnName = "username")},
            inverseJoinColumns={@JoinColumn(name = "groupname", referencedColumnName = "groupname")}
    )
    private SystemUserGroup usergroup;

    public SystemUser() {}
    
    public SystemUser(String username, String userpassword) {
        this.username = username;
        this.userpassword = userpassword;
        this.name = null;
        this.surname = null;
    }
    
    public SystemUser(String username, String userpassword, String name, String surname) {
        this.username = username;
        this.userpassword = userpassword;
        this.name = name;
        this.surname = surname;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 89 * hash + Objects.hashCode(this.id);
        hash = 89 * hash + Objects.hashCode(this.username);
        hash = 89 * hash + Objects.hashCode(this.userpassword);
        hash = 89 * hash + Objects.hashCode(this.name);
        hash = 89 * hash + Objects.hashCode(this.surname);
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
        final SystemUser other = (SystemUser) obj;
        if (!Objects.equals(this.username, other.username)) {
            return false;
        }
        if (!Objects.equals(this.userpassword, other.userpassword)) {
            return false;
        }
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        if (!Objects.equals(this.surname, other.surname)) {
            return false;
        }
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return userpassword;
    }

    public void setPassword(String password) {
        this.userpassword = password;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SystemUserGroup getUsergroup() {
        return usergroup;
    }

    public void setUsergroup(SystemUserGroup usergroup) {
        this.usergroup = usergroup;
    }
}
