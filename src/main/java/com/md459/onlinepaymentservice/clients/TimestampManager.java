/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.md459.onlinepaymentservice.clients;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
    
    SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");
    
    public Date getTimestamp() throws TException, ParseException {
        
        Date timestamp;
        
        try {
            TTransport transport;
            
            transport = new TSocket("localhost", 10001);
            transport.open();
            
            TProtocol protocol = new TBinaryProtocol(transport);
            TimestampService.Client client = new TimestampService.Client(protocol);
            
            timestamp = sdf.parse(client.timestamp());
            transport.close();
            
            return timestamp;
        } catch(TException e) {
            throw e;
        }
    }
}
