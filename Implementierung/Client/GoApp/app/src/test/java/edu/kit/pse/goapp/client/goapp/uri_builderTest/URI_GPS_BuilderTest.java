package edu.kit.pse.goapp.client.goapp.uri_builderTest;

import org.junit.Test;
import static org.junit.Assert.*;

import java.net.URI;

import edu.kit.pse.client.goapp.uri_builder.URI_GPS_Builder;

/**
 * Created by paula on 15.07.16.
 */
public class URI_GPS_BuilderTest {

    @Test
    public void builderTest() {
        URI_GPS_Builder builder = new URI_GPS_Builder();
        URI uri = builder.getURI();
        URI testUri = null;
        try {
            testUri = new URI("https://i43pc164.ipd.kit.edu/PSESoSe16GoGruppe2/GoAppServer/Gps");
        } catch (java.net.URISyntaxException e){
            //cant happen
        }
        assertEquals(testUri,uri);
    }

    @Test
    public void addParameterTest() {
        URI_GPS_Builder builder = new URI_GPS_Builder();
        builder.addParameter("testKey","testValue");
        URI uri = builder.getURI();
        URI testUri= null;
        try {
            testUri = new URI("https://i43pc164.ipd.kit.edu/PSESoSe16GoGruppe2/GoAppServer/Gps?testKey=testValue");
        } catch (java.net.URISyntaxException e){
            //cant happen
        }
        assertEquals(testUri,uri);
    }
}
