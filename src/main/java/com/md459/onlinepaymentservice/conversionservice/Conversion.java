/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.md459.onlinepaymentservice.conversionservice;

import java.util.HashMap;
import java.util.Map;
import javax.ejb.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author marko
 */
@Singleton
@Path("conversion")
public class Conversion {
    
    Map<String, Map<String, Float>> rates;
    
    public Conversion() {
        rates = new HashMap<>();
        
        addRate("GBP", "USD", 1.24f);
        addRate("USD", "GBP", 1/getRate("GBP", "USD"));
        addRate("GBP", "EUR", 1.14f);
        addRate("EUR", "GBP", 1/getRate("GBP", "EUR"));
        addRate("EUR", "USD", 1.09f);
        addRate("USD", "EUR", 1/getRate("EUR", "USD"));
        
    }
    
    private void addRate(String currency1, String currency2, float rate) {
        if(!rates.containsKey(currency1)) rates.put(currency1, new HashMap<>());
        
        rates.get(currency1).put(currency2, rate);
    }
    
    private float getRate(String currency1, String currency2) {
        return rates.get(currency1).get(currency2);
    }
    
    @GET
    @Path("{currency1}/{currency2}/{amount_of_currency1}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getConvertedAmount(@PathParam("currency1")String c1, @PathParam("currency2")String c2, @PathParam("amount_of_currency1")String amount) {
            float amountOfCurrency2 = Float.valueOf(amount) * getRate(c1, c2);
            
            return Response.ok(amountOfCurrency2).build();
    }
}
