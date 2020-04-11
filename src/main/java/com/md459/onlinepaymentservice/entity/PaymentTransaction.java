/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.md459.onlinepaymentservice.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

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
    private SystemUser fromUser;
    
    @ManyToOne
    private SystemUser toUser;
    
    private float amount;
    private String currency;
    private String status;
    private String description;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationTime;
    
    public PaymentTransaction() {}
    
    public PaymentTransaction(SystemUser fromUser, SystemUser toUser, float amount, String description) {
        this.fromUser = fromUser;
        this.toUser = toUser;
        this.amount = amount;
        this.description = description;
        this.status = "PENDING";
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }
    
    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
