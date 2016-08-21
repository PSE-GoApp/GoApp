package edu.kit.pse.client.goapp.receiver;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import edu.kit.pse.client.goapp.CommunicationKeys;
import edu.kit.pse.client.goapp.activity.SettingsActivity;
import edu.kit.pse.client.goapp.converter.ObjectConverter;
import edu.kit.pse.client.goapp.databaseadapter.DataBaseAdapter;
import edu.kit.pse.client.goapp.datamodels.GPS;
import edu.kit.pse.client.goapp.datamodels.Meeting;
import edu.kit.pse.client.goapp.service.GPS_Service;

/**
 * Created by PSE
 */
public class AlarmReceiver extends BroadcastReceiver implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private LocationManager locationManager;
    private final String tag = "AlarmReceiver";
    private Context context;
    private Double lat, lon;
    private final static String COUNTER = "counter";
    private GoogleApiClient googleApiClient;
    private LatLng cordinates;
    private DataBaseAdapter dataBaseAdapter;
    private int meetingId;
    private Intent intent;
    private Meeting meeting;

    protected GoogleApiClient mGoogleApiClient;

    /**
     * Stores parameters for requests to the FusedLocationProviderApi.
     */
    protected LocationRequest mLocationRequest;

    /**
     * onReceive handler. Starts the gps service
     *
     * @param context from activity
     * @param intent  from activity
     */
    @Override
    public void onReceive(Context context, Intent intent) {

        this.context = context;
        this.intent = intent;
        meetingId = intent.getIntExtra(CommunicationKeys.MEETING_ID, -1);
        if (meetingId == -1) {
            cancelAlarm();
            Log.d(tag, "Cancel Meeting Intent Extra was empty");
            return;
        }

        dataBaseAdapter = new DataBaseAdapter(context);
        meeting = dataBaseAdapter.getMeeting(meetingId);
        if (meeting == null) {
            cancelAlarm();
            Log.d(tag, "Cancel Meeting: " + meetingId + "\n Meeting = null");
            return;
        }

        Log.d(tag, "Hallo from Receiver: " + meetingId
                + "\nTimeStamp = " + meeting.getTimestamp() + "\nDuration = " + meeting.getDuration());


        if (System.currentTimeMillis() >= meeting.getTimestamp() + meeting.getDuration() * 60 * 1000) {
            cancelAlarm();
            Log.d(tag, "Cancel Meeting: " + meetingId + "\nMeeting expired");
            return;
        }

        if (gpsOkUser(context)) {
            Log.d(tag, "Cancel Meeting: " + meetingId + "\nSettings-GPS-off");
            cancelAlarm();
            return;
        }

        googleApiClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        if (googleApiClient != null) {
            Log.d(tag, "connecting");
            googleApiClient.connect();
        }

        /*
        if (counter(context)) {
            SharedPreferences sharedpreferences = context.getSharedPreferences(COUNTER, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putInt(COUNTER, 1);
            editor.commit();
            */

        // TODO TEST THIS !!--------------------------------------------------------------------------------------------
        LatLng latLng = getLocation();
        if (latLng != null) {
            Log.d(tag, "Latitude (Y): " + latLng.latitude+ ", Longitude (X): " + latLng.longitude);

            startingGPSServicePut(latLng);

            /*int statusCode = sentGPS(latLng);
            switch (statusCode) {
                case 400:
                    cancelAlarm();
            }*/

        } else {
            Log.d(tag, "No Location got.");
        }
        // TODO --------------------------------------------------------------------------------------------------------

    }

    public void cancelAlarm() {

        // wrong intent
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, meetingId, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
    }

    /**
     * gest location
     *
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

            Log.d(tag, "no permissions");
            return null;
        } else {
            Log.d(tag, "permissions");
            //if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

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
                Log.d(tag, "lat: " + lat);
                Log.d(tag, "lon: " + lon);
                return new LatLng(lat, lon);
            } catch (NullPointerException e) {
                Log.d(tag, "something went wrong");
                Log.e(tag, "no GPS(" + e.getMessage() + ")");
                return null;
            } catch (Exception e) {
                Log.d(tag, "something went wrong:" + e.toString());

            }

            // } else {
            //  Log.d("Made","not provided");

            //}
        }
        return null;
    }

    /**
     * checks if the user ist ok with sharing gps cordinates
     *
     * @param context from activity
     * @return true if he doesn't want to share
     */
    private boolean gpsOkUser(Context context) {
        SharedPreferences sharedpreferences = context.getSharedPreferences(SettingsActivity.GPSENABLED, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();

        // default value = 0, because they dont set the Settings => send GPS
        int i = sharedpreferences.getInt(SettingsActivity.GPSENABLED, 1);
        if (i == 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * checks if it should continue sending gps cordinates
     *
     * @param context from activity
     * @return true if should stop
     */
    private boolean counter(Context context) {
        Log.d(tag, "counter");
        SharedPreferences sharedpreferences = context.getSharedPreferences(COUNTER, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();

        int i = sharedpreferences.getInt(COUNTER, 121);
        Log.d(tag, "counter:" + i);
        //TODO is not todo: duration the number multiplied by the time interval for the alarm
        if (i > 3) {
            Log.d(tag, "counter true");
            return true;
        } else {
            editor.putInt(COUNTER, i + 1);
            editor.commit();
            Log.d(tag, "counter false");
            return false;
        }
    }

    /**
     * gets the meeting id
     *
     * @param // context from activity
     * @return the id
     */

    /*
    private int getId(Context context) {
        SQLiteDatabase db = context.openOrCreateDatabase("MeetingStarts.db", Context.MODE_PRIVATE, null);
        final Cursor cursor = db.rawQuery("SELECT " + DataBaseHandler.COLUMN_MEETING_ID + " FROM " + DataBaseHandler.TABLE_MEETING + " ORDER BY " + DataBaseHandler.COLUMN_TIMESTAMP + " ASC LIMIT 1", null);
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
        db.delete("MeetingStarts.db", DataBaseHandler.COLUMN_MEETING_ID + " =" + sum, null);
        return sum;
    }
    */

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
    @Override
    public void onConnected(Bundle bundle) {
        Log.d("Made", "Connected to Google Play Services!");

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
            if (lastLocation == null) {
                return;
            }
            double lat = lastLocation.getLatitude(), lon = lastLocation.getLongitude();
            Log.d(tag, " here are:" + lat + ", " + lon);
            cordinates = new LatLng(lat, lon);
        } else {
            Log.d(tag, "connected? ");
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i(tag, "Can't connect to Google Play Services!");
    }


/*
    public int sentGPS(LatLng latLng) {

        Log.d(tag, "Sending GPS");

        Boolean noError = true;
        Boolean result = true;
        GPS gps = new GPS(latLng.longitude, latLng.latitude, 0);

        ObjectConverter<GPS> gpsObjectConverter = new ObjectConverter<>();

        String GPSAsJsonString = gpsObjectConverter.serialize(gps, GPS.class);
        HttpResponse closeableHttpResponse = null;

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
            int statusCode = closeableHttpResponse.getStatusLine().getStatusCode();
            Log.d(tag, "Successful execut! StatusCode=" + statusCode);
            return statusCode;

        } else {
            return 500;
        }
    }
    */

    private void startingGPSServicePut(LatLng latLng) {
        GPS gps = new GPS(latLng.longitude, latLng.latitude, 0);
        ObjectConverter<GPS> gpsObjectConverter = new ObjectConverter<>();

        String GPSAsJsonString = gpsObjectConverter.serialize(gps, GPS.class);

        Intent i = new Intent(context, GPS_Service.class);
        i.putExtra(CommunicationKeys.GPS, GPSAsJsonString);
        i.putExtra(CommunicationKeys.COMMAND, CommunicationKeys.PUT);
        context.startService(i);

    }
}


