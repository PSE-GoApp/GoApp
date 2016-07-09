package edu.kit.pse.client.goapp.httpappclient;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;

public class HttpAppClientPost extends HttpAppClient{

	private HttpPost request;
	public HttpAppClientPost()
	{
		super();
	}
	public void setUri(URI uri)
	{
		request = new HttpPost(uri);
	    request.addHeader("content-type", "application/json");
	    request.addHeader("Accept","application/json");
	}
	public void setBody(String jsonData) throws UnsupportedEncodingException
	{
		   StringEntity params = new StringEntity(jsonData);
		   request.setEntity(params);

	}
	public CloseableHttpResponse executeRequest() throws ClientProtocolException, IOException
	{
		return client.execute(request);
	}
}
