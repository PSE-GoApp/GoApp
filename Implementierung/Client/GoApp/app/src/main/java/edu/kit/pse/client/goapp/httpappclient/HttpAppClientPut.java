package edu.kit.pse.client.goapp.httpappclient;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;

public class HttpAppClientPut extends HttpAppClient{

	private HttpPut request;
	public HttpAppClientPut()
	{
		super();
	}
	public void setUri(URI uri)
	{synchronized (this) {
		request = new HttpPut(uri);
	    request.addHeader("content-type", "application/json");
	    request.addHeader("Accept","application/json");
	}}
	public void setBody(String jsonData) throws UnsupportedEncodingException
	{
		synchronized (this) {
			StringEntity params = new StringEntity(jsonData);
			request.setEntity(params);
		}
	}
	public HttpResponse executeRequest() throws ClientProtocolException, IOException
	{
		synchronized (this) {
			return client.execute(request);
		}
	}
}
