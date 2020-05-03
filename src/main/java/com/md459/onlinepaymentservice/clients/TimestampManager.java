/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.md459.onlinepaymentservice.clients;

import java.util.Date;
import javax.ejb.Singleton;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;

/**
 *
 * @author marko
 */
@Singleton
public class TimestampManager {
    
    public Date getTimestamp() throws TException {
        Date timestamp;
        
        try {
            TTransport transport;
            
            transport = new TSocket("localhost", 10001);
            transport.open();
            
            TProtocol protocol = new TBinaryProtocol(transport);
            TimestampService.Client client = new TimestampService.Client(protocol);
            
            timestamp = new Date(client.timestamp());
            
            transport.close();
            
            return timestamp;
        } catch(TException e) {
            throw e;
        }
    }
    
}
