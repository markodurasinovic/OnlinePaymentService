package com.md459.onlinepaymentservice.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * A DTO for PaymentTransaction entities.
 * 
 * Provides getter methods to allow field access in JSF.
 */
public class PaymentTransactionTO implements Serializable {
    public Long id;
    public SystemUserTO payer;
    public SystemUserTO payee;
    public float amount;
    public String currency;
    public String status;
    public String description;
    public Date creationTime;
    
    public PaymentTransactionTO() {}
    
    public PaymentTransactionTO(SystemUserTO payer, SystemUserTO payee, float amount, String description, String currency) {
        this.payer = payer;
        this.payee = payee;
        this.amount = amount;
        this.description = description;
        this.currency = currency;
        this.status = "PENDING";
    }
    
    // Getters so that DTO's fields can be used in JSF

    public Long getId() {
        return id;
    }

    public SystemUserTO getPayer() {
        return payer;
    }

    public SystemUserTO getPayee() {
        return payee;
    }

    public float getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }

    public String getStatus() {
        return status;
    }

    public String getDescription() {
        return description;
    }

    public Date getCreationTime() {
        return creationTime;
    }
}
