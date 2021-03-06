package com.md459.onlinepaymentservice.clients;

import javax.ejb.EJBException;
import javax.ejb.Singleton;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
* ConversionClient sends requests to the Conversion service's API.
*/
@Singleton
public class ConversionClient {    
        
    private final Client client;
    private final String baseUri = "http://localhost:10000/webapps2020/services/conversion";
    
    public ConversionClient() {
        client = ClientBuilder.newClient();
    }
    
    /**
     * Send a request to convert amount from currency1 to currency2.
     * Extracts the float value from the response and returns it.
     * 
     * @param currency1 - Currency to convert from.
     * @param currency2 - Currency to convert to.
     * @param amount - Amount to convert
     * @return Converted amount.
     */
    public float getConvertedAmount(String currency1, String currency2, float amount) {
        Invocation.Builder builder = client.target(baseUri)
                .path(currency1)
                .path(currency2)
                .path(String.valueOf(amount))
                .request(MediaType.APPLICATION_JSON);
        
        Response res = builder.get();
        int status = res.getStatus();
        if(status == 200) {
            return res.readEntity(float.class);
        } else {
            throw new EJBException(res.readEntity(String.class));
        }
    }
}
