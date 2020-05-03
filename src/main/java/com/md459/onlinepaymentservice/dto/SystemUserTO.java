package com.md459.onlinepaymentservice.dto;

import java.io.Serializable;
import java.util.List;

/**
 * A DTO for SystemUser entities.
 * 
 * Provides getter methods to allow field access in JSF.
 */
public class SystemUserTO implements Serializable {
    public Long id;
    public String username;
    public String userpassword;
    public String name;
    public String surname;
    public String currency;
    public float balance;
    public SystemUserGroupTO usergroup;
    public List<PaymentTransactionTO> fromTransactions;
    public List<PaymentTransactionTO> toTransactions;
    
    public SystemUserTO() {}
    
    public SystemUserTO(String username, String userpassword, String name, String surname, float initialBalance, String currency) {
        this.username = username;
        this.userpassword = userpassword;
        this.name = name;
        this.surname = surname;
        this.balance = initialBalance;
        this.currency = currency;
    }
    
    public SystemUserTO(String username, String userpassword) {
        this.username = username;
        this.userpassword = userpassword;
    }
    
    // Getters so that DTO's fields can be used in JSF

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getUserpassword() {
        return userpassword;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getCurrency() {
        return currency;
    }

    public float getBalance() {
        return balance;
    }

    public SystemUserGroupTO getUsergroup() {
        return usergroup;
    }

    public List<PaymentTransactionTO> getFromTransactions() {
        return fromTransactions;
    }

    public List<PaymentTransactionTO> getToTransactions() {
        return toTransactions;
    }
}
