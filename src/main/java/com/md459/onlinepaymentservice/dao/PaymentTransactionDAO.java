package com.md459.onlinepaymentservice.dao;

import com.md459.onlinepaymentservice.dto.PaymentTransactionTO;
import com.md459.onlinepaymentservice.dto.SystemUserTO;
import java.util.List;
import javax.ejb.Local;

@Local
public interface PaymentTransactionDAO {
    public void insert(PaymentTransactionTO transaction);    
    public void update(PaymentTransactionTO transaction);
    public List<PaymentTransactionTO> getAll();
    public List<PaymentTransactionTO> getHistory(SystemUserTO user);
    public PaymentTransactionTO getById(long id);
    public List<PaymentTransactionTO> getPaymentRequests(SystemUserTO user);
}
