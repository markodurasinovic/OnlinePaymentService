/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.md459.timestampservice;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import org.apache.thrift.server.TServer.Args;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TSimpleServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;

/**
 *
 * @author marko
 */
@Startup
@Singleton
public class TimestampServer {
    
    public static TimestampHandler handler;
    public static TimestampService.Processor processor;
    public static TServerTransport serverTransport;
    public static TServer server;
    
    public TimestampServer() {}
    
    @PostConstruct
    public void init() {
        try {
            handler = new TimestampHandler();
            processor = new TimestampService.Processor(handler);
            
            Runnable simple = new Runnable() {
                @Override
                public void run() {
                    simple(processor);
                }
            };
            
            new Thread(simple).start();
        } catch(Exception e) {
            System.err.println(e);
        }
    }
    
    
    private static void simple(TimestampService.Processor processor) {
        try {
            serverTransport = new TServerSocket(10001);
            server = new TSimpleServer(new Args(serverTransport).processor(processor));
            
            server.serve();
        } catch(Exception e) {
            System.err.println(e);
        }
    }
    
    @PreDestroy
    public void stop() {
        server.stop();
    }
}
