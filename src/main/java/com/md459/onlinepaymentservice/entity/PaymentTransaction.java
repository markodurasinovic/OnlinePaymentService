package com.md459.onlinepaymentservice.entity;

import com.md459.onlinepaymentservice.dto.PaymentTransactionTO;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class PaymentTransaction implements Serializable {

    @Id
    @GeneratedValue
    private Long id;
    
    @ManyToOne
    private SystemUser payer;
    
    @ManyToOne
    private SystemUser payee;
    
    private float amount;
    private String currency;
    private String status;
    private String description;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationTime;
    
    public PaymentTransaction() {}
    
    public PaymentTransaction(float amount, String description, String currency, String status) {
        this.amount = amount;
        this.description = description;
        this.currency = currency;
        this.status = status != null ? status : "PENDING";
    }
    
    /**
     * Create a DTO with this PaymentTransaction entity's values.
     * 
     * @return - A PaymentTransaction DTO.
     */
    public PaymentTransactionTO getTransactionData() {
        return createPaymentTransactionTO();
    }

    private PaymentTransactionTO createPaymentTransactionTO() {
        PaymentTransactionTO trans = new PaymentTransactionTO();
        trans.id = id;
        trans.payer = payer.getUserData();
        trans.payee = payee.getUserData();
        trans.amount = amount;
        trans.currency = currency;
        trans.status = status;
        trans.description = description;
        trans.creationTime = creationTime;
        
        return trans;
    }
    
    /**
     * Update this PaymentTransaction entity with updatedTrans DTO's 
     * values.
     * 
     * @param updatedTrans - A PaymentTransaction DTO.
     */
    public void setTransactionData(PaymentTransactionTO updatedTrans) {
        mergeTransactionData(updatedTrans);
    }
    
    private void mergeTransactionData(PaymentTransactionTO updatedTrans) {
        payer.setUserData(updatedTrans.payer);
        payee.setUserData(updatedTrans.payee);
        amount = updatedTrans.amount;
        currency = updatedTrans.currency;
        status = updatedTrans.status;
        description = updatedTrans.description;
        creationTime = updatedTrans.creationTime;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.id);
        hash = 59 * hash + Objects.hashCode(this.payer);
        hash = 59 * hash + Objects.hashCode(this.payee);
        hash = 59 * hash + Float.floatToIntBits(this.amount);
        hash = 59 * hash + Objects.hashCode(this.currency);
        hash = 59 * hash + Objects.hashCode(this.status);
        hash = 59 * hash + Objects.hashCode(this.description);
        hash = 59 * hash + Objects.hashCode(this.creationTime);
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
        final PaymentTransaction other = (PaymentTransaction) obj;
        if (Float.floatToIntBits(this.amount) != Float.floatToIntBits(other.amount)) {
            return false;
        }
        if (!Objects.equals(this.currency, other.currency)) {
            return false;
        }
        if (!Objects.equals(this.status, other.status)) {
            return false;
        }
        if (!Objects.equals(this.description, other.description)) {
            return false;
        }
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.payer, other.payer)) {
            return false;
        }
        if (!Objects.equals(this.payee, other.payee)) {
            return false;
        }
        if (!Objects.equals(this.creationTime, other.creationTime)) {
            return false;
        }
        return true;
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

    public SystemUser getPayer() {
        return payer;
    }

    public void setPayer(SystemUser payer) {
        this.payer = payer;
    }

    public SystemUser getPayee() {
        return payee;
    }

    public void setPayee(SystemUser payee) {
        this.payee = payee;
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
