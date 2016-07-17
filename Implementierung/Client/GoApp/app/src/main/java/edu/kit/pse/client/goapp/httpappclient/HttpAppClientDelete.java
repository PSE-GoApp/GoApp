package edu.kit.pse.client.goapp.httpappclient;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.io.DefaultHttpResponseWriter;

public class HttpAppClientDelete extends HttpAppClient{

	private HttpDelete request;
	public HttpAppClientDelete()
	{
		super();
	}
	public void setUri(URI uri)
	{
		request = new HttpDelete(uri);
	}

	public HttpResponse executeRequest() throws ClientProtocolException, IOException
	{

		return client.execute(request);
	}
}
