package edu.kit.pse.client.goapp.httpappclient;



import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;
import java.net.URI;

/**
 * This class comunicates with the server. It returns HttpResponses, that contain a Statuscode, which indicates if the request was successful, and the requested objects as String.
 * Created by paula on 01.07.16.
 */
public class HttpAppClient {

    private static CloseableHttpClient client;

    public HttpAppClient() {
        if (client == null){
            client = HttpClientBuilder.create().build();
        }
    }

    /**
     * Sends a HTTP POST Request to the server
     * @param uri Gives the URL of the needed servlet and contains Parameters
     */
    public HttpResponse sendPost(URI uri) throws IOException {
        CloseableHttpResponse response;
        HttpPost request = new HttpPost(uri);
        response = client.execute(request);
        return new HttpResponse(response);
    }

    /**
     * Sends a HTTP GET Request to the server
     * @param uri Gives the URL of the needed servlet and contains Parameters
     */
    public HttpResponse sendGet(URI uri) throws IOException {
        CloseableHttpResponse response;
        HttpGet request = new HttpGet(uri);
        response = client.execute(request);
        return new HttpResponse(response);
    }

    /**
     * Sends a HTTP DELETE Request to the server
     * @param uri Gives the URL of the needed servlet and contains Parameters
     */
    public HttpResponse sendDelete(URI uri) throws IOException {
        CloseableHttpResponse response;
        HttpDelete request = new HttpDelete(uri);
        response = client.execute(request);
        return new HttpResponse(response);
    }

    /**
     * Sends a HTTP PUT Request to the server
     * @param uri Gives the URL of the needed servlet and contains Parameters
     */
    public HttpResponse sendPut(URI uri) throws IOException {
        CloseableHttpResponse response;
        HttpPut request = new HttpPut(uri);
        response = client.execute(request);
        return new HttpResponse(response);
    }

}
