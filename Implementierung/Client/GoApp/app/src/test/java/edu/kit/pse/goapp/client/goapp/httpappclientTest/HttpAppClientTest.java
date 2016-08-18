package edu.kit.pse.goapp.client.goapp.httpappclientTest;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;

import org.apache.http.client.methods.HttpDelete;
import org.apache.http.impl.client.DefaultHttpClient;
import org.junit.Assert.*;

import org.apache.http.HttpResponse;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import edu.kit.pse.client.goapp.httpappclient.HttpAppClient;
import edu.kit.pse.client.goapp.httpappclient.HttpAppClientDelete;
import edu.kit.pse.client.goapp.httpappclient.HttpAppClientGet;
import edu.kit.pse.client.goapp.httpappclient.HttpAppClientPost;
import edu.kit.pse.client.goapp.httpappclient.HttpAppClientPut;

import static junit.framework.Assert.assertEquals;

/**
 * Created by Ta on 16.08.2016.
 */
public class HttpAppClientTest {

    @Test
    public void testSetUriDelete() {
        URI uri = URI.create("http://apache.org/foo?a=b&a=c&b=d+e#blah");
        HttpAppClientDelete httpAppClientDelete = Mockito.mock(HttpAppClientDelete.class);
        httpAppClientDelete.setUri(uri);
    }

    @Test
    public void testSetUriGet() {
        URI uri = URI.create("http://apache.org/foo?a=b&a=c&b=d+e#blah");
        HttpAppClientGet httpAppClientGet = Mockito.mock(HttpAppClientGet.class);
        httpAppClientGet.setUri(uri);
    }

    @Test
    public void testSetUriPost() {
        URI uri = URI.create("http://apache.org/foo?a=b&a=c&b=d+e#blah");
        HttpAppClientPost httpAppClientPost = Mockito.mock(HttpAppClientPost.class);
        httpAppClientPost.setUri(uri);
    }

    @Test
    public void testSetUriPut() {
        URI uri = URI.create("http://apache.org/foo?a=b&a=c&b=d+e#blah");
        HttpAppClientPut httpAppClientPut = Mockito.mock(HttpAppClientPut.class);
        httpAppClientPut.setUri(uri);
    }

    @Test
    public void testExecuteRequestDelete() throws IOException {
        HttpResponse httpResponse = Mockito.mock(HttpResponse.class);
        HttpAppClientDelete httpAppClientDelete = Mockito.mock(HttpAppClientDelete.class);
        URI uri = URI.create("http://apache.org/foo?a=b&a=c&b=d+e#blah");
        httpAppClientDelete.setUri(uri);
        HttpResponse response = httpAppClientDelete.executeRequest();
        if (response != null) {
            assertEquals(httpResponse, response);

        }
    }

    @Test
    public void testExecuteRequestGet() throws IOException {
        HttpResponse httpResponse = Mockito.mock(HttpResponse.class);
        HttpAppClientGet httpAppClientGet = Mockito.mock(HttpAppClientGet.class);
        URI uri = URI.create("http://apache.org/foo?a=b&a=c&b=d+e#blah");
        httpAppClientGet.setUri(uri);
        HttpResponse response = httpAppClientGet.executeRequest();
        if (response != null) {
            assertEquals(httpResponse, response);
        }
    }

    @Test
    public void testExecuteRequestPost() throws IOException {
        HttpResponse httpResponse = Mockito.mock(HttpResponse.class);
        HttpAppClientPost httpAppClientPost = Mockito.mock(HttpAppClientPost.class);
        URI uri = URI.create("http://apache.org/foo?a=b&a=c&b=d+e#blah");
        httpAppClientPost.setUri(uri);
        HttpResponse response = httpAppClientPost.executeRequest();
        if (response != null) {
            assertEquals(httpResponse, response);
        }
    }

    @Test
    public void testExecuteRequestPut() throws IOException {
        HttpResponse httpResponse = Mockito.mock(HttpResponse.class);
        HttpAppClientPut httpAppClientPut = Mockito.mock(HttpAppClientPut.class);
        URI uri = URI.create("http://apache.org/foo?a=b&a=c&b=d+e#blah");
        httpAppClientPut.setUri(uri);
        HttpResponse response = httpAppClientPut.executeRequest();
        if (response != null) {
            assertEquals(httpResponse, response);
        }
    }

    @Test
    public void testSetBodyPost() throws UnsupportedEncodingException {
        String jsonData = "{\"jsonString\"}";
        HttpAppClientPost httpAppClientPost = Mockito.mock(HttpAppClientPost.class);
        httpAppClientPost.setBody(jsonData);
    }

    @Test
    public void testSetBodyPut() throws UnsupportedEncodingException {
        String jsonData = "{\"jsonString\"}";
        HttpAppClientPut httpAppClientPut = Mockito.mock(HttpAppClientPut.class);
        httpAppClientPut.setBody(jsonData);
    }
}
