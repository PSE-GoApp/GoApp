package edu.kit.pse.client.goapp.uri_builder;

import org.apache.http.client.utils.URIBuilder;

import java.net.URI;

/**
 * Created by Ta on 01.07.2016.
 */
public class URI_UserBuilder extends ClientURI_Builder {

    private static final String servletAdd = "User"; //we don't know it yet
    private URIBuilder uribuilder;
    private URI uri;

    public URI_UserBuilder() {
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
