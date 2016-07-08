package edu.kit.pse.client.goapp.httpappclient;



import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

public abstract class HttpAppClient {

    protected static CloseableHttpClient client;
    public HttpAppClient() {
        if (client == null){
             client = HttpClientBuilder.create().build();
        }
    }
    

}
