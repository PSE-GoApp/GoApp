package edu.kit.pse.client.goapp.uri_builder;

import org.apache.http.client.utils.URIBuilder;

import java.net.URI;

/**
 * Created by paula on 29.06.16.
 */
public class URI_LoginBuilder extends ClientURI_Builder {

    private static final String servletAdd = "Login"; //we don't know it yet
    private URIBuilder uribuilder;
    private URI uri;

    public URI_LoginBuilder(){
            uribuilder = new URIBuilder();
            uribuilder.setPath(serverAdd+servletAdd);
    }

    @Override
    public void addParameter(String key, String value) {
        uribuilder.addParameter(key, value);
    }

    @Override
    public URI getURI() {
        try {
            uri = uribuilder.build();
        } catch (java.net.URISyntaxException syntaxException){
            //handle it somehow
        }
        return uri;
    }
}
