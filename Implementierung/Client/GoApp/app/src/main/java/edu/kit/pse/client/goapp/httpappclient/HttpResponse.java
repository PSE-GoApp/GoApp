package edu.kit.pse.client.goapp.httpappclient;

import org.apache.http.client.methods.CloseableHttpResponse;

/**This Class contains the responces of the Server, which are returned in all Methods of the HttpAppClient
 * Created by paula on 01.07.16.
 */
public class HttpResponse {
    private static CloseableHttpResponse response;

    public HttpResponse(CloseableHttpResponse response){
        this.response = response;
    }

    public int getStatusCode() {
        //need to understand the ClosableHttpResponse first
        return 0;
    }
}
