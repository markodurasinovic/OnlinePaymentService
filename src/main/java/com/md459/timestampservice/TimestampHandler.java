package com.md459.timestampservice;

import java.util.Date;
import org.apache.thrift.TException;

/**
 * Provides timestamping functionality.
 */
public class TimestampHandler implements TimestampService.Iface {

    /**
     * Returns the current timestamp as string.
     * 
     * @return - A string representing the current date and time.
     * @throws TException 
     */
    @Override
    public String timestamp() throws TException {
        return new Date().toString();
    }
    
}
