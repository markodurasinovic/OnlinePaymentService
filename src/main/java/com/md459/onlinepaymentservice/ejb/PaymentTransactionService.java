package com.md459.onlinepaymentservice.ejb;

import com.md459.onlinepaymentservice.dto.PaymentTransactionTO;
import com.md459.onlinepaymentservice.dto.SystemUserTO;
import java.util.List;
import javax.ejb.Local;

@Local
public interface PaymentTransactionService {
    
    public List<PaymentTransactionTO> getAllTransactions();
    public void acceptRequest(long reqId);
    public void rejectRequest(long reqId);
    public List<PaymentTransactionTO> getTransactionHistory(SystemUserTO user);
    public void requestPayment(String requester, String requestee, float amount, String description);
    public int getNumRequests(SystemUserTO user);
    public List<PaymentTransactionTO> getPaymentRequests(SystemUserTO user);
    public void makePayment(String payer, String payee, float amount, String description);
}
