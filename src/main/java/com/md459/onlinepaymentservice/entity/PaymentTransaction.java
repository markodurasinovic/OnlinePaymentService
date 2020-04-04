/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.md459.onlinepaymentservice.entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 *
 * @author marko
 */
@Entity
public class PaymentTransaction implements Serializable {

    @Id
    @GeneratedValue
    private Long id;
    
    @ManyToOne
    SystemUser fromUser;
    
    @ManyToOne
    SystemUser toUser;
    
    double amount;
    
    public PaymentTransaction() {}
    
    public PaymentTransaction(SystemUser fromUser, SystemUser toUser, double amount) {
        this.fromUser = fromUser;
        this.toUser = toUser;
        this.amount = amount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SystemUser getFromUser() {
        return fromUser;
    }

    public void setFromUser(SystemUser fromUser) {
        this.fromUser = fromUser;
    }

    public SystemUser getToUser() {
        return toUser;
    }

    public void setToUser(SystemUser toUser) {
        this.toUser = toUser;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
    
}
