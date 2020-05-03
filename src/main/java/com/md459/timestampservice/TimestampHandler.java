/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.md459.timestampservice;

import java.util.Date;
import org.apache.thrift.TException;

/**
 *
 * @author marko
 */
public class TimestampHandler implements TimestampService.Iface {

    @Override
    public String timestamp() throws TException {
        return new Date().toString();
    }
    
}
