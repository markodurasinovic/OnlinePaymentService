/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.md459.onlinepaymentservice.dao;

import com.md459.onlinepaymentservice.entity.PaymentTransaction;
import com.md459.onlinepaymentservice.entity.SystemUser;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author marko
 */
@Local
public interface PaymentTransactionDAO {
    public long insert(PaymentTransaction transaction);    
    public boolean update(PaymentTransaction transaction);
    public List<PaymentTransaction> getAll();
    public List<PaymentTransaction> getHistory(SystemUser user);
    public PaymentTransaction getById(long id);
    public List<PaymentTransaction> getPaymentRequests(SystemUser user);
}
