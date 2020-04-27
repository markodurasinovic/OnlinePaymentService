/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.md459.onlinepaymentservice.dto;

import com.md459.onlinepaymentservice.entity.SystemUserGroup;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author marko
 */
public class SystemUserTO implements Serializable {
    public Long id;
    public String username;
    public String userpassword;
    public String name;
    public String surname;
    public String currency;
    public float balance;
    public SystemUserGroup usergroup;
    public List<PaymentTransactionTO> fromTransactions;
    public List<PaymentTransactionTO> toTransactions;
    
    public SystemUserTO() {}
}
