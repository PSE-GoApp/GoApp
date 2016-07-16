package edu.kit.pse.goapp.client.goapp.uri_builderTest;

import org.junit.Test;

import java.net.URI;

import edu.kit.pse.client.goapp.uri_builder.URI_GPS_Builder;
import edu.kit.pse.client.goapp.uri_builder.URI_MeetingParticipantManagementBuilder;

import static org.junit.Assert.assertEquals;

/**
 * Created by paula on 16.07.16.
 */
public class URI_MeetingParticipantManagementBuilderTest {
    @Test
    public void builderTest() {
        URI_MeetingParticipantManagementBuilder builder = new URI_MeetingParticipantManagementBuilder();
        URI uri = builder.getURI();
        URI testUri = null;
        try {
            testUri = new URI("http://localhost:8080/GoAppServer/MeetingParticipantManagement");
        } catch (java.net.URISyntaxException e){
            //cant happen
        }
        assertEquals(testUri,uri);
    }

    @Test
    public void addParameterTest() {
        URI_MeetingParticipantManagementBuilder builder = new URI_MeetingParticipantManagementBuilder();
        builder.addParameter("testKey","testValue");
        URI uri = builder.getURI();
        URI testUri= null;
        try {
            testUri = new URI("http://localhost:8080/GoAppServer/MeetingParticipantManagement?testKey=testValue");
        } catch (java.net.URISyntaxException e){
            //cant happen
        }
        assertEquals(testUri,uri);
    }
}
