package edu.kit.pse.goapp.client.goapp.uri_builderTest;

import org.junit.Test;

import java.net.URI;

import edu.kit.pse.client.goapp.uri_builder.URI_GPS_Builder;
import edu.kit.pse.client.goapp.uri_builder.URI_NotificationsBuilder;

import static org.junit.Assert.assertEquals;

/**
 * Created by paula on 16.07.16.
 */
public class URI_NotificationsBuilderTest {
    @Test
    public void builderTest() {
        URI_NotificationsBuilder builder = new URI_NotificationsBuilder();
        URI uri = builder.getURI();
        URI testUri = null;
        try {
            testUri = new URI("https://i43pc164.ipd.kit.edu/PSESoSe16GoGruppe2/GoAppServer/Notifications");
        } catch (java.net.URISyntaxException e){
            //cant happen
        }
        assertEquals(testUri,uri);
    }

    @Test
    public void addParameterTest() {
        URI_NotificationsBuilder builder = new URI_NotificationsBuilder();
        builder.addParameter("testKey","testValue");
        URI uri = builder.getURI();
        URI testUri= null;
        try {
            testUri = new URI("https://i43pc164.ipd.kit.edu/PSESoSe16GoGruppe2/GoAppServer/Notifications?testKey=testValue");
        } catch (java.net.URISyntaxException e){
            //cant happen
        }
        assertEquals(testUri,uri);
    }
}
