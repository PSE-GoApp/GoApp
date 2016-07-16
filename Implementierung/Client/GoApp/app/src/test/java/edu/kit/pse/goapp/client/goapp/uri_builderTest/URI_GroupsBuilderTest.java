package edu.kit.pse.goapp.client.goapp.uri_builderTest;

import org.junit.Test;

import java.net.URI;

import edu.kit.pse.client.goapp.uri_builder.URI_GPS_Builder;
import edu.kit.pse.client.goapp.uri_builder.URI_GroupsBuilder;

import static org.junit.Assert.assertEquals;

/**
 * Created by paula on 16.07.16.
 */
public class URI_GroupsBuilderTest {
    @Test
    public void builderTest() {
        URI_GroupsBuilder builder = new URI_GroupsBuilder();
        URI uri = builder.getURI();
        URI testUri = null;
        try {
            testUri = new URI("http://localhost:8080/GoAppServer/Groups");
        } catch (java.net.URISyntaxException e){
            //cant happen
        }
        assertEquals(testUri,uri);
    }

    @Test
    public void addParameterTest() {
        URI_GroupsBuilder builder = new URI_GroupsBuilder();
        builder.addParameter("testKey","testValue");
        URI uri = builder.getURI();
        URI testUri= null;
        try {
            testUri = new URI("http://localhost:8080/GoAppServer/Groups?testKey=testValue");
        } catch (java.net.URISyntaxException e){
            //cant happen
        }
        assertEquals(testUri,uri);
    }
}
