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
* TimestampClient sends requests to the timestamp thrift service.
*/
@Singleton
public class TimestampClient {      
    
    SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");
    
    /**
     * Sends a request to TimestampService server, retrieving the current 
     * date and time (timestamp) in String format. This timestamp is parsed
     * and returned as a Date.
     * 
     * @return - A timestamp.
     * @throws TException - In case of a failed thrift request.
     * @throws ParseException - In case of the SimpleDateFormat parser being 
     *                          unable to provide the Date String
     *                          provided by the server.
     */
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
        } catch(TException | ParseException e) {
            throw e;
        }
    }
}
