package edu.kit.pse.client.goapp.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

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

import edu.kit.pse.client.goapp.CommunicationKeys;
import edu.kit.pse.client.goapp.ServiceResultReceiver;
import edu.kit.pse.client.goapp.converter.ObjectConverter;
import edu.kit.pse.client.goapp.datamodels.GPS;
import edu.kit.pse.client.goapp.datamodels.Meeting;
import edu.kit.pse.client.goapp.datamodels.Participant;
import edu.kit.pse.client.goapp.service.MeetingService;
import edu.kit.pse.goapp.client.goapp.R;

public class MapActivity extends AppCompatActivity
        implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        ServiceResultReceiver.Receiver,
        View.OnClickListener, PopupMenu.OnMenuItemClickListener{

    private List<GPS> gps = new ArrayList<GPS>();
    public ServiceResultReceiver mReceiver;
    private List<Marker> marker = new ArrayList<Marker>();

    private ObjectConverter<Meeting> mConverter =  new ObjectConverter<>();

    private GoogleMap mMap;
    private MapView mapView;
    private int REQUEST_LOCATION = 2;
    GoogleApiClient mGoogleApiClient;
    /**
     * Represents a geographical location.
     */
    private Location mLastLocation;
    static double lat;
    static double lng;
    static int meetingId;
    private ImageButton menu_button;


    /**
     * on Create method that every activity has
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        menu_button = (ImageButton) findViewById(R.id.menu_map);
        menu_button.setOnClickListener(this);
        mapView = (MapView) findViewById(R.id.map_view);
        mapView.onCreate(savedInstanceState);
        mapView.setClickable(true);
        mapView.getMapAsync(this);
        test(this);
        buildGoogleApiClient();
    }

    /**
     * Starts the Activity
     * @param activity: the activity that is calling it
     */
    public static void start(Activity activity, int meetingID, Double latit, Double longit) {
        Intent intent = new Intent(activity, MapActivity.class);
        activity.startActivity(intent);
        lat = latit;
        lng = longit;
        meetingId = meetingID;
    }

    /**
     * teste if location is turned on and gives you the dialog window to enable it
     * @param context
     */
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

    /**
     * Handler for the request Permissions(because of android 6 permissions are asked on runtime)
     * @param requestCode the code
     * @param permissions the permission wanted
     * @param grantResults results
     */
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
            // permission was granted, yay!

        } else {

            // permission denied, boo!
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

    /**
     * start for the mGoogleApiClient
     */
    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    /**
     * stop for the mGoogleApiClient
     */
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


    /**
     * on suspend for the mGoogleApiClient
     * @param cause
     */
    @Override
    public void onConnectionSuspended(int cause) {
        // The connection to Google Play services was lost for some reason. We call connect() to
        // attempt to re-establish the connection.
        mGoogleApiClient.connect();
    }

    /**
     * asks for the promissions on runtime
     */
    private void getPromissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Check Permissions Now
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION);
            Log.d("Made", "fuck you");
        } else {
            Log.d("Made", "fuck yeah!");
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
        startServiceGetParticipant();
    }

    /**
     * destroy map
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    /**
     * what to du when the memory is low
     */
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    /**
     * calls the super onPause when the app is paused
     */
    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    /**
     * calls the super when the app resums
     */
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    /**
     * calls on super.onSaveInstancesSTate when the state needs to be saved
     * @param outState
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    /**
     * handler for the communication with the service
     * @param resultCode
     * @param resultData
     */
    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {

        if (resultCode == 200) {
            if (resultData.getString(CommunicationKeys.SERVICE) == CommunicationKeys.FROM_MEETING_SERVICE){

                String jString = resultData.getString(CommunicationKeys.MEETING);
                List<Participant> participants;
                Meeting meeting;
                meeting = mConverter.deserialize(jString, Meeting.class);
                participants = meeting.getParticipants();

                gps = new ArrayList<>();

                for (Participant p : participants) {
                    GPS tempGPS = p.getUser().getGps();
                    if (tempGPS != null) {
                        gps.add(tempGPS);
                    }
                }
                updateMap();

            } else {
                // should not called
                Toast.makeText(this, "Error: falscher Service", Toast.LENGTH_LONG).show();

            }
        } else {
            Toast.makeText(this, "Error: " + resultCode, Toast.LENGTH_LONG).show();
        }
    }



    private void updateMap() {
        mMap.clear();
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        if (gps.size() == 0) {
            Marker mark = mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(lat, lng))
                    .title("Treffpunkt"));
            marker.add(mark);
            builder.include(new LatLng(lat, lng));

        } else {
            for (GPS g : gps) {
                Marker mark = mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(g.getX(), g.getY()))
                        .title("Freund"));
                marker.add(mark);

                for (Marker m : marker) {
                    builder.include(m.getPosition());
                }

            }
        }
        LatLngBounds bounds = builder.build();
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 30);
        mMap.moveCamera(cu);
    }

    /**
     * asks for the GPS cordinates
     */
    public void startServiceGetParticipant() {
        //which service?
        Intent i = new Intent(this, MeetingService.class);
        mReceiver = new ServiceResultReceiver(new Handler());
        mReceiver.setReceiver(this);
        i.putExtra(CommunicationKeys.MEETING_ID, meetingId);
        i.putExtra(CommunicationKeys.RECEICER, mReceiver);
        i.putExtra(CommunicationKeys.COMMAND, "GET");
        startService(i);
    }

    /**
     * on click listener for the acitvity
     * @param v
     */
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.menu_map) {
            showPopUp(v);
        }
    }

    /**
     * on click listener for the menu
     * @param item the item clicked
     * @return true if item was clicked
     */
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home_groups:
                MeetingListActivity.start(this);
                return true;
            case R.id.neuer_termin_groups:
                CreateNewMeetingActivity.start(this);
                return true;
            case R.id.settings_groups:
                SettingsActivity.start(this);
                return true;
            case R.id.about_groups:
                AboutActivity.start(this);
                return true;
            default:
                return false;
        }
    }

    /**
     * method that shows up the popUpMenu
     * @param v the view
     */
    public void showPopUp(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.setOnMenuItemClickListener(MapActivity.this);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_map, popup.getMenu());
        popup.show();
    }
}
