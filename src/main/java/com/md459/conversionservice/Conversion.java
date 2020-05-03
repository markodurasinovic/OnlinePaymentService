package com.md459.conversionservice;

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
 * RESTful Conversion service provided at /services/conversion
 */
@Singleton
@Path("conversion")
public class Conversion {    
    
    Map<String, Map<String, Float>> rates;
    
    public Conversion() {
        rates = new HashMap<>();
        
        addRate("GBP", "USD", 1.24f);
        addRate("GBP", "EUR", 1.14f);
        addRate("EUR", "USD", 1.09f);        
    }
    
    /**
     * Adds exchange rates from currency1 to currency2, and from currency2
     * to currency1, based on the rate provided to the method.
     * 
     * @param currency1
     * @param currency2
     * @param rate 
     */
    private void addRate(String currency1, String currency2, float rate) {
        if(!rates.containsKey(currency1)) rates.put(currency1, new HashMap<>());
        if(!rates.containsKey(currency2)) rates.put(currency2, new HashMap<>());
        
        rates.get(currency1).put(currency2, rate);
        rates.get(currency2).put(currency1, 1/rate);
    }
    
    private float getRate(String currency1, String currency2) {
        float rate;
        if(currency1.equals(currency2)) {
            rate = 1f;
        } else {
            rate = rates.get(currency1).get(currency2);
        }
        return rate;
    }
    
    /**
     * Convert amount from c1 to c2, based on values in rates.
     * 
     * @param c1
     * @param c2
     * @param amount
     * @return 
     */
    @GET
    @Path("{currency1}/{currency2}/{amount_of_currency1}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getConvertedAmount(@PathParam("currency1")String c1, @PathParam("currency2")String c2, @PathParam("amount_of_currency1")String amount) {
            if(!rates.containsKey(c1)) {
                return Response.status(Response.Status.NOT_IMPLEMENTED)
                        .entity("Conversion not implemented for currency " + c1)
                        .build();
            } else if(!rates.containsKey(c2)) {
                return Response.status(Response.Status.NOT_IMPLEMENTED)
                        .entity("Conversion not implemented for currency " + c2)
                        .build();
            } else {
                try {
                    float amountOfCurrency2 = Float.valueOf(amount) * getRate(c1, c2);
                    return Response.ok(amountOfCurrency2).build();
                } catch(NumberFormatException e) {
                    return Response.status(Response.Status.BAD_REQUEST)
                            .entity("Value: " + amount + " is invalid. Must be float.")
                            .build();
                }
            }            
    }
}
