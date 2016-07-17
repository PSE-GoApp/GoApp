package edu.kit.pse.client.goapp.httpappclient;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;

import java.io.IOException;
import java.net.URI;

public class HttpAppClientGet extends HttpAppClient {

	private HttpGet request;
	public HttpAppClientGet()
	{
		super();
	}
	public void setUri(URI uri)
	{
		request = new HttpGet(uri);
	}
	public HttpResponse executeRequest() throws ClientProtocolException, IOException
	{synchronized (this) {
		return client.execute(request);
	}
	}
	
	
}
