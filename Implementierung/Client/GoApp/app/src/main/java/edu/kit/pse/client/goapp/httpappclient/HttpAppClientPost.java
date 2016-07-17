package edu.kit.pse.client.goapp.httpappclient;

import android.util.Log;

import org.apache.http.HttpResponse;
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

	/**
	 * sets uri builder
	 * @param uri
     */
	public void setUri(URI uri)
 	{  synchronized (this) {
		request = new HttpPost(uri);
		request.addHeader("content-type", "application/json");
		request.addHeader("Accept", "application/json");
	}
	}

	/**
	 * sets the body
	 * @param jsonData jsonString
	 * @throws UnsupportedEncodingException
     */
	public void setBody(String jsonData) throws UnsupportedEncodingException
	{ synchronized (this) {
		StringEntity params = new StringEntity(jsonData);
		request.setEntity(params);
	}
	}

	/**
	 * execute request
	 * @return http responce
	 * @throws ClientProtocolException
	 * @throws IOException
     */
	public HttpResponse executeRequest() throws ClientProtocolException, IOException
	{
		synchronized (this) {		Log.e("try"," trysend");
			HttpResponse r= client.execute(request);
		Log.e("try"," finished send");
		return r;
	}}
}
