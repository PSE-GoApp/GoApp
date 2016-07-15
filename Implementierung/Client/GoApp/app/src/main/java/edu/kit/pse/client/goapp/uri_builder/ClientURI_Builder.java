package edu.kit.pse.client.goapp.uri_builder;

import java.net.URI;

/**Contains a private static final String serverAdd, which contains the Serveradress and the private Field URI, in which the URI is stored, is not needed.
 * Created by paula on 29.06.16.
 */
public abstract class ClientURI_Builder {

    //The serveradress
    protected static final String serverAdd = "http://localhost:8080/GoAppServer/";

    /**
     * Adds Parameters to the private URI
     * @param key key
     * @param value value
     */
    public abstract void addParameter(String key, String value);

    /**
     * Returns the local URI_Object
     * @return
     */
    public abstract URI getURI();
}
