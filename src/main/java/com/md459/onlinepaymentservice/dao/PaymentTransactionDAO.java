/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.md459.onlinepaymentservice.dao;

import com.md459.onlinepaymentservice.dto.PaymentTransactionTO;
import com.md459.onlinepaymentservice.dto.SystemUserTO;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author marko
 */
@Local
public interface PaymentTransactionDAO {
    public void insert(PaymentTransactionTO transaction);    
    public void update(PaymentTransactionTO transaction);
    public List<PaymentTransactionTO> getAll();
    public List<PaymentTransactionTO> getHistory(SystemUserTO user);
    public PaymentTransactionTO getById(long id);
    public List<PaymentTransactionTO> getPaymentRequests(SystemUserTO user);
}
