package edu.kit.pse.client.goapp.receiver;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;

import java.io.IOException;

import edu.kit.pse.client.goapp.CommunicationKeys;
import edu.kit.pse.client.goapp.activity.SettingsActivity;
import edu.kit.pse.client.goapp.databasehandler.DataBaseHandler;
import edu.kit.pse.client.goapp.httpappclient.HttpAppClientPut;
import edu.kit.pse.client.goapp.uri_builder.URI_GPS_Builder;

/**
 * Created by PSE
 */
public class AlarmReceiver extends BroadcastReceiver {

    private LocationManager locationManager;
    Context context;
    Double lat, lon;
    private final static String COUNTER = "counter";


    /**
     * onReceive handler. Starts the gps service
     * @param context from activity
     * @param intent from activity
     */
    @Override
    public void onReceive(Context context, Intent intent) {

        this.context = context;

        if(gpsOkUser(context)){
            return;
        }

        if(counter(context)){
            SharedPreferences sharedpreferences = context.getSharedPreferences(COUNTER, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putInt(COUNTER, 1);
            editor.commit();


            //
            int id = getId(context);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, id, intent, 0);
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            alarmManager.cancel(pendingIntent);
        }

        Boolean noError = true;
        Boolean result = true;

        Toast.makeText(context, "Good Morning!", Toast.LENGTH_LONG).show();


        String GPSAsJsonString = null;
        HttpResponse closeableHttpResponse = null;

        final ResultReceiver resultReceiver = intent.getParcelableExtra(CommunicationKeys.RECEICER);

        Bundle bundle = new Bundle();
        bundle.putString(CommunicationKeys.COMMAND, CommunicationKeys.PUT);
        bundle.putString(CommunicationKeys.SERVICE, CommunicationKeys.FROM_GPS_SERVICE);

        GPSAsJsonString = intent.getStringExtra(CommunicationKeys.GPS);

        URI_GPS_Builder uri_gps_builder = new URI_GPS_Builder();

        HttpAppClientPut httpAppClientPut = new HttpAppClientPut();
        httpAppClientPut.setUri(uri_gps_builder.getURI());
        try {
            httpAppClientPut.setBody(GPSAsJsonString);
        } catch (IOException e) {
            //Todo Handle Exception. Maybe the String Extra was null
            result = false;
        }

        try {
            // TODO catch 404 (No Internet and Request Time out)
            closeableHttpResponse = httpAppClientPut.executeRequest();
        } catch (IOException e) {
            // TODO handle Exception Toast? Alert Dialog? sent it to the Activity?
            noError = false;
        }

        if (result && noError) {
            if (closeableHttpResponse.getStatusLine().getStatusCode() == 200) {

            }

        }
    }

    /**
     * gest location
     * @return users cordinates
     */
    //TODO not tested and maybe not ready. To be honest the person who wrote it can not even remember what he did!
    public LatLng getLocation() {
        // Get the location manager
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

            // check Permissons
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            // todo Notivication
            return null;
        } else {

            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

                // todo a new Location Listener
                // LocationListener locationListener = new MyLocationListener();


                Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                // 5000 = minimum time interval between location updates, in milliseconds
                // 10 = minimum distance between location updates, in meters
                //  locationManager.requestLocationUpdates(
                //         LocationManager.GPS_PROVIDER, 5000, 10, locationListener);
                Location gotLoc = locationManager
                        .getLastKnownLocation(LocationManager.GPS_PROVIDER);

                try {
                    lat = location.getLatitude();
                    lon = location.getLongitude();
                    return new LatLng(lat, lon);
                } catch (NullPointerException e) {
                    Log.e("Receiver", "no GPS(" + e.getMessage() + ")");
                    return null;
                }

            }
        }
        return null;
    }

    /**
     * checks if the user ist ok with sharing gps cordinates
     * @param context from activity
     * @return true if he doesn't want to share
     */
    private boolean gpsOkUser(Context context){
        SharedPreferences sharedpreferences = context.getSharedPreferences(SettingsActivity.GPSENABLED, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();

        int i = sharedpreferences.getInt(SettingsActivity.GPSENABLED, 3);
        if (i == 0){
            return true;
        }else{
            return false;
        }
    }

    /**
     * checks if it should continue sending gps cordinates
     * @param context from activity
     * @return true if should stop
     */
    private boolean counter(Context context) {
        SharedPreferences sharedpreferences = context.getSharedPreferences(COUNTER, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();

        int i = sharedpreferences.getInt(COUNTER, 121);
        if (i > 120){
            return true;
        }else{
            return false;
        }
    }

    /**
     * gets the meeting id
     * @param context from activity
     * @return the id
     */
    private int getId(Context context){
        SQLiteDatabase db = context.openOrCreateDatabase("MeetingStarts.db", Context.MODE_PRIVATE, null);
        final Cursor cursor = db.rawQuery("SELECT "+ DataBaseHandler.COLUMN_MEETING_ID+" FROM " +DataBaseHandler.TABLE_MEETING + " ORDER BY "+DataBaseHandler.COLUMN_TIMESTAMP+" ASC LIMIT 1", null);
        int sum = -1;
        if (cursor != null) {
            try {
                if (cursor.moveToFirst()) {
                    sum = cursor.getInt(0);
                }
            } finally {
                cursor.close();
            }
        }
        db.delete("MeetingStarts.db",DataBaseHandler.COLUMN_MEETING_ID + " =" + sum,null);
        return sum;
    }

    // Listener class to get coordinates
    /*
    private class MyLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location loc) {
            editLocation.setText("");
            pb.setVisibility(View.INVISIBLE);
            Toast.makeText(
                    getBaseContext(),
                    "Location changed: Lat: " + loc.getLatitude() + " Lng: "
                            + loc.getLongitude(), Toast.LENGTH_SHORT).show();
            String longitude = "Longitude: " + loc.getLongitude();
            Log.v(TAG, longitude);
            String latitude = "Latitude: " + loc.getLatitude();
            Log.v(TAG, latitude);

        //To get city name from coordinates
            String cityName = null;
            Geocoder gcd = new Geocoder(getBaseContext(), Locale.getDefault());
            List<Address> addresses;
            try {
                addresses = gcd.getFromLocation(loc.getLatitude(),
                        loc.getLongitude(), 1);
                if (addresses.size() > 0) {
                    System.out.println(addresses.get(0).getLocality());
                    cityName = addresses.get(0).getLocality();
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            String s = longitude + "\n" + latitude + "\n\nMy Current City is: "
                    + cityName;
            editLocation.setText(s);
        }

        @Override
        public void onProviderDisabled(String provider) {}

        @Override
        public void onProviderEnabled(String provider) {}

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {}
    }
    */


}























