/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.md459.onlinepaymentservice.ejb;

import javax.ejb.Singleton;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author marko
 */
@Singleton
public class ConversionManager {
    
    private Client client;
    private final String baseUri = "http://localhost:10000/webapps2020/services/conversion";
    
    public ConversionManager() {
        client = ClientBuilder.newClient();
    }
    
    public float getConvertedAmount(String currency1, String currency2, float amount) {
        float convertedAmount = client.target(baseUri)
                    .path(currency1)
                    .path(currency2)
                    .path(String.valueOf(amount))
                    .request(MediaType.APPLICATION_JSON)
                    .get(Float.class);

        return convertedAmount;        
    }
}
