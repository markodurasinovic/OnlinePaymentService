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
    private String currency;
    private double balance;
    
    @ManyToOne
    private SystemUserGroup usergroup;
    
    @OneToMany(mappedBy = "fromUser", cascade = CascadeType.PERSIST)
    private List<PaymentTransaction> fromTransactions;
    
    @OneToMany(mappedBy = "toUser", cascade = CascadeType.PERSIST)
    private List<PaymentTransaction> toTransactions;

    public SystemUser() {}
    
    // Admin registration
    public SystemUser(String username, String userpassword) {
        this.username = username;
        this.userpassword = userpassword;
        this.name = null;
        this.surname = null;
        this.currency = null;
        this.balance = 0;
    }
    
    public SystemUser(String username, String userpassword, String name, String surname, String currency) {
        this.username = username;
        this.userpassword = userpassword;
        this.name = name;
        this.surname = surname;
        this.currency = currency;
        this.balance = initBalance();
    }

    public String getUserpassword() {
        return userpassword;
    }

    public void setUserpassword(String userpassword) {
        this.userpassword = userpassword;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
    
    private double initBalance() {
        // this should use REST instead
        double defVal = 1000; // GBP
        double USDRate = 1.23;
        double EURRate = 1.14;
        switch(this.currency) {
            case "USD":
                return defVal * USDRate;
            case "EUR":
                return defVal * EURRate;
            default:
                return defVal;
        }
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

    public List<PaymentTransaction> getFromTransactions() {
        return fromTransactions;
    }

    public void setFromTransactions(List<PaymentTransaction> fromTransactions) {
        this.fromTransactions = fromTransactions;
    }
    
    public void addFromTransaction(PaymentTransaction transaction) {
        if(fromTransactions == null) fromTransactions = new ArrayList<>();
        
        fromTransactions.add(transaction);
    }

    public List<PaymentTransaction> getToTransactions() {
        return toTransactions;
    }

    public void setToTransactions(List<PaymentTransaction> toTransactions) {
        this.toTransactions = toTransactions;
    }
    
    public void addToTransaction(PaymentTransaction transaction) {
        if(toTransactions == null) toTransactions = new ArrayList<>();
        
        toTransactions.add(transaction);
    }
}
