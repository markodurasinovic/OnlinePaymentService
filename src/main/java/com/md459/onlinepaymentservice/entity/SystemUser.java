package com.md459.onlinepaymentservice.entity;

import com.md459.onlinepaymentservice.dto.PaymentTransactionTO;
import com.md459.onlinepaymentservice.dto.SystemUserTO;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

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
    private float balance;
    
    @ManyToOne
    private SystemUserGroup usergroup;
    
    @OneToMany(mappedBy = "payer", cascade = CascadeType.PERSIST)
    private List<PaymentTransaction> fromTransactions;
    
    @OneToMany(mappedBy = "payee", cascade = CascadeType.PERSIST)
    private List<PaymentTransaction> toTransactions;

    public SystemUser() {}
    
    // Admin registration
    public SystemUser(String username, String userpassword) {
        this.username = username;
        this.userpassword = userpassword;
        this.name = null;
        this.surname = null;
        this.currency = null;
        this.balance = 0f;
    }
    
    // User registration
    public SystemUser(String username, String userpassword, String name, String surname, float initialBalance, String currency) {
        this.username = username;
        this.userpassword = userpassword;
        this.name = capitalise(name);
        this.surname = capitalise(surname);
        this.balance = initialBalance;
        this.currency = currency;
    }
    
    /**
     * Create a DTO with this SystemUser entity's values.
     * 
     * @return - A SystemUser DTO.
     */
    public SystemUserTO getUserData() {
        return createSystemUserTO();
    }
    
    private SystemUserTO createSystemUserTO() {
        SystemUserTO user = new SystemUserTO();
        user.id = id;
        user.username = username;
        user.userpassword = userpassword;
        user.name = name;
        user.surname = surname;
        user.currency = currency;
        user.balance = balance;
        user.fromTransactions = getTransactionTOs(fromTransactions);
        user.toTransactions = getTransactionTOs(toTransactions);
        
        return user;
    }
    
    private List<PaymentTransactionTO> getTransactionTOs(List<PaymentTransaction> transactions) {
        List<PaymentTransactionTO> transactionTOs = new ArrayList<>();
        transactions.forEach((t) -> {
            transactionTOs.add(t.getTransactionData());
        });
        
        return transactionTOs;
    }
    
    /**
     * Update this SystemUser entity with updatedTrans DTO's 
     * values.
     * 
     * @param updatedUser - A SystemUser DTO.
     */
    public void setUserData(SystemUserTO updatedUser) {
        mergeUserData(updatedUser);
    }
    
    private void mergeUserData(SystemUserTO updatedUser) {
        username = updatedUser.username;
        userpassword = updatedUser.userpassword;
        name = updatedUser.name;
        surname = updatedUser.surname;
        currency = updatedUser.currency;
        balance = updatedUser.balance;
        fromTransactions = updateTransactions(updatedUser.fromTransactions);
        toTransactions = updateTransactions(updatedUser.toTransactions);
    }
    
    /**
     * Update all of this SystemUser entity's transactions through the DTOs.
     * 
     * @param transactionTOs
     * @return 
     */
    private List<PaymentTransaction> updateTransactions(List<PaymentTransactionTO> transactionTOs) {
        List<PaymentTransaction> updatedTransactions = new ArrayList<>();
        transactionTOs.forEach((t) -> {
            PaymentTransaction updatedTrans = new PaymentTransaction();
            updatedTrans.setTransactionData(t);
            updatedTransactions.add(updatedTrans);
        });
        
        return updatedTransactions;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + Objects.hashCode(this.id);
        hash = 83 * hash + Objects.hashCode(this.username);
        hash = 83 * hash + Objects.hashCode(this.userpassword);
        hash = 83 * hash + Objects.hashCode(this.name);
        hash = 83 * hash + Objects.hashCode(this.surname);
        hash = 83 * hash + Objects.hashCode(this.currency);
        hash = 83 * hash + Float.floatToIntBits(this.balance);
        hash = 83 * hash + Objects.hashCode(this.usergroup);
        hash = 83 * hash + Objects.hashCode(this.fromTransactions);
        hash = 83 * hash + Objects.hashCode(this.toTransactions);
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
        if (Float.floatToIntBits(this.balance) != Float.floatToIntBits(other.balance)) {
            return false;
        }
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
        if (!Objects.equals(this.currency, other.currency)) {
            return false;
        }
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.usergroup, other.usergroup)) {
            return false;
        }
        if (!Objects.equals(this.fromTransactions, other.fromTransactions)) {
            return false;
        }
        if (!Objects.equals(this.toTransactions, other.toTransactions)) {
            return false;
        }
        return true;
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

    public float getBalance() {
        return balance;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }
    
    public void setBalanceAndCurrency(float balance, String currency) {
        this.balance = balance;
        this.currency = currency;
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
        this.name = capitalise(name);
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = capitalise(surname);
    }
    
    private String capitalise(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
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
