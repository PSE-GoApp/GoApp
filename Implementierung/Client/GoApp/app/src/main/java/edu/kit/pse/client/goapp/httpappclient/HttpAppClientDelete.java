package edu.kit.pse.client.goapp.httpappclient;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpDelete;

import java.io.IOException;
import java.net.URI;

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

	/**
	 * http request for delete
	 * @return http responce
	 * @throws ClientProtocolException
	 * @throws IOException
     */
	public HttpResponse executeRequest() throws ClientProtocolException, IOException
	{synchronized (this) {

		return client.execute(request);
	}
	}
}
