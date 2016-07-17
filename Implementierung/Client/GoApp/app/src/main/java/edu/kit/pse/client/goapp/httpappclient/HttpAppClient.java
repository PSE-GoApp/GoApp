package edu.kit.pse.client.goapp.httpappclient;



import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

/**
 * abstract class for the hhttp client
 */
public abstract class HttpAppClient {

     protected static DefaultHttpClient client;

    /**
     * http client
     */
    public HttpAppClient() {
        synchronized (this) {
            if (client == null) {
                   client = new DefaultHttpClient();

            }


        }
    }
}
