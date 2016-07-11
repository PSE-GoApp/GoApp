package edu.kit.pse.client.goapp.activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import edu.kit.pse.client.goapp.CommunicationKeys;
import edu.kit.pse.client.goapp.ServiceResultReceiver;
import edu.kit.pse.client.goapp.datamodels.GPS;
import edu.kit.pse.goapp.client.goapp.R;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class MapActivity extends AppCompatActivity
        implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        ServiceResultReceiver.Receiver{

    private List<GPS> gps = new ArrayList<GPS>();
    public ServiceResultReceiver mReceiver;
    private List<Marker> marker = new ArrayList<Marker>();

    private GoogleMap mMap;
    private MapView mapView;
    private int REQUEST_LOCATION = 2;
    GoogleApiClient mGoogleApiClient;
    /**
     * Represents a geographical location.
     */
    private Location mLastLocation;
    double lat;
    double lng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        mapView = (MapView) findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        mapView.setClickable(true);
        mapView.getMapAsync(this);
        test(this);
        buildGoogleApiClient();
    }



    private void test(Context context){
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if( !locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ) {
            LayoutInflater layoutInflater = LayoutInflater.from(MapActivity.this);
            View promptView = layoutInflater.inflate(R.layout.map_pop_up, null);

            android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(MapActivity.this);
            alertDialogBuilder.setView(promptView);



            alertDialogBuilder.setCancelable(false)
                    .setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    Intent myIntent = new Intent( Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                    MapActivity.this.startActivity(myIntent);
                                }
                            }
                    )
                    .setNegativeButton("Cancel",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

            // create an alert dialog
            android.app.AlertDialog alert = alertDialogBuilder.create();
            alert.show();
            return;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {

        // If request is cancelled, the result arrays are empty.
        if (grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            try {
                mMap.setMyLocationEnabled(true);
            } catch (final SecurityException ex) {
            }
            // permission was granted, yay! Do the
            // contacts-related task you need to do.

        } else {

            // permission denied, boo! Disable the
            // functionality that depends on this permission.
        }
        return;

    }

    /**
     * Builds a GoogleApiClient. Uses the addApi() method to request the LocationServices API.
     */
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }


    /**
     * Runs when a GoogleApiClient object successfully connects.
     */
    @Override
    public void onConnected(Bundle connectionHint) {
        // Provides a simple way of getting a device's location and is well suited for
        // applications that do not require a fine-grained location and that do not need location
        // updates. Gets the best and most recent location currently available, which may be null
        // in rare cases when a location is not available.
        getPromissions();
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        // Refer to the javadoc for ConnectionResult to see what error codes might be returned in
        // onConnectionFailed.
        Log.i("Some", "Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());
    }


    @Override
    public void onConnectionSuspended(int cause) {
        // The connection to Google Play services was lost for some reason. We call connect() to
        // attempt to re-establish the connection.
        mGoogleApiClient.connect();
    }
    private void getPromissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Check Permissions Now
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION);
        } else {
            mMap.setMyLocationEnabled(true);
            // permission has been granted, continue as usual
            Location myLocation =
                    LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if (myLocation != null) {
                /*mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(myLocation.getLatitude(), myLocation.getLongitude()))
                        .title("My Location"));*/
            }
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setIndoorEnabled(true);
        // getPromissions();
        getGPS();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        //FILL the gps list
        //ArrayList<ParcelableGPS> parcelableGps = resultData.getParcelableArrayList("GPS");
       /* if (parcelableGps != null) {
            for (ParcelableGPS g : parcelableGps) {
                gps.add(g.getGps());
            }
        }*/
        mMap.clear();
        if (gps.size() == 0) {
            Marker mark = mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(0, 0))
                    .title("Marker"));
            marker.add(mark);
        } else {
            for (GPS g : gps) {
                Marker mark = mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(g.getX(), g.getY()))
                        .title("Marker"));
                marker.add(mark);

                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                if (lat != 0) {
                    Log.e("Something", "is not working");
                    builder.include(new LatLng(lat, lng));
                }
                for (Marker m : marker) {
                    builder.include(m.getPosition());
                }
                LatLngBounds bounds = builder.build();
                CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 30);
                mMap.moveCamera(cu);
            }
        }
    }

    public void getGPS() {
        //which service?
        Intent i = new Intent(this, MapActivity.class);
        mReceiver = new ServiceResultReceiver(new Handler());
        mReceiver.setReceiver(this);
        i.putExtra(CommunicationKeys.RECEICER, mReceiver);
        i.putExtra(CommunicationKeys.COMMAND, "GET");
        startService(i);
    }
}
