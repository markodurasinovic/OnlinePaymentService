/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.md459.onlinepaymentservice.dto;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author marko
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
}
