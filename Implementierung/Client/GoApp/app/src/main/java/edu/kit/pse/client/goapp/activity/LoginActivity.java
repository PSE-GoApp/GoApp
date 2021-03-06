package edu.kit.pse.client.goapp.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;

import edu.kit.pse.client.goapp.CommunicationKeys;
import edu.kit.pse.client.goapp.ServiceResultReceiver;
import edu.kit.pse.client.goapp.converter.ObjectConverter;
import edu.kit.pse.client.goapp.datamodels.User;
import edu.kit.pse.client.goapp.service.LoginService;
import edu.kit.pse.client.goapp.service.UserService;
import edu.kit.pse.goapp.client.goapp.R;

/**
 * Created by PSE on 10.07.16.
 */
public class LoginActivity extends AppCompatActivity implements  ServiceResultReceiver.Receiver, GoogleApiClient.OnConnectionFailedListener,  View.OnClickListener, GoogleApiClient.ConnectionCallbacks {


    private static final String TAG = "LoginActivity";
    private static final int RC_SIGN_IN = 9001;
    private static final int RC_REGISTER = 9002;

    private ServiceResultReceiver activityReceiver;
    private String userFullName;
    private String userIdToken;
    private String googleId;

    private static final int PERMISSION_ACCESS_COARSE_LOCATION = 1;

    ObjectConverter<User> userConvert;

    private GoogleApiClient loginGoogleApiClient;

    private ProgressDialog mProgressDialog;

    private EditText newUserName;

    /**
     * Wir aufgerufen wen diese Activity erstellt wird
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        newUserName = (EditText) findViewById(R.id.tip_NewUserName);
        userConvert = new ObjectConverter<>();

        // Button listeners
        findViewById(R.id.signInButton).setOnClickListener(this);
        findViewById(R.id.registerButton).setOnClickListener(this);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.server_client_id))
                .requestEmail()
                .build();

        // [START build_client]
        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.
        loginGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        // [END build_client]


        // [START customize_button]
        // Customize sign-in button. The sign-in button can be displayed in
        // multiple sizes and color schemes. It can also be contextually
        // rendered based on the requested scopes. For example. a red button may
        // be displayed when Google+ scopes are requested, but a white button
        // may be displayed when only basic profile is requested. Try adding the
        // Scopes.PLUS_LOGIN scope to the GoogleSignInOptions to see the
        // difference.
        SignInButton signInButton = (SignInButton) findViewById(R.id.signInButton);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        signInButton.setScopes(gso.getScopeArray());
        // [END customize_button]

         SignInButton registerButton = (SignInButton) findViewById(R.id.registerButton);
        registerButton.setSize(SignInButton.SIZE_STANDARD);
        registerButton.setScopes(gso.getScopeArray());
    }

    /*
    @Override
    public void onStart() {
        super.onStart();

        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(loginGoogleApiClient);
        if (opr.isDone()) {
            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
            // and the GoogleSignInResult will be available instantly.
            Log.d(TAG, "Got cached sign-in");
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        } else {
            // If the user has not previously signed in on this device or the sign-in has expired,
            // this asynchronous branch will attempt to sign in the user silently.  Cross-device
            // single sign-on will occur in this branch.
            showProgressDialog();
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                    handleSignInResult(googleSignInResult);
                }
            });
        }
    }
    */

    // [START handleSignInResult]
    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            userFullName = acct.getDisplayName();
            userIdToken = acct.getIdToken();
            googleId = acct.getId();

            goAppLogin();




        } else {
            hideProgressDialog();
            // Signed out, show unauthenticated UI.
        }
    }
    // [END handleSignInResult]


    /**
     * click handler
     * @param v view was angeklickt wurde
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.signInButton:
                // block nr. 12,7
                showProgressDialog();

                signIn();

                break;
            case R.id.registerButton:
                if (!newUserName.getText().toString().isEmpty()) {
                    // Todo AlertDiaglog sure to create a new member?

                    // block Buttons from here NR.42
                    showProgressDialog();

                    register();
                    // Todo test -----------------
                //    User newUser = new User(-1, newUserName.getText().toString());
              //       startnewUserService(newUser);

                } else {
                    // TOdo AlertBuilder
                    Toast.makeText(this, "Ein Name muss gesetzt sein", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    /**
     * erstellt ein Google Service um sich anzumelden
     */
    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(loginGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    /**
     * erstellt ein google Service, um sich im Google anzumelden. Das Ergebnis wir anders gehand habt
     */
    private void register() {
        Intent registerIntent = Auth.GoogleSignInApi.getSignInIntent(loginGoogleApiClient);
        startActivityForResult(registerIntent, RC_REGISTER);
    }


    /**
     *  START onActivityResult from Google
     * @param requestCode request Code for the Google
     * @param resultCode result code from Google
     * @param data result Data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);

        switch (requestCode) {
            case RC_SIGN_IN:
                GoogleSignInResult resultSign = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                Log.d(TAG, "onActivityResult:GET_TOKEN:success:" + resultSign.getStatus().isSuccess());

                int statusCode = resultSign.getStatus().getStatusCode();
                Log.e("LoginActivity", "Google Statuscode:" + statusCode );
                if (resultSign.isSuccess()) {
                    Log.e("LoginActivity", "Result success, Statuscode: " + statusCode );
                    GoogleSignInAccount acct = resultSign.getSignInAccount();
                    // Get account information
                    userFullName = acct.getDisplayName();
                    userIdToken = acct.getIdToken();
                    googleId = acct.getId();

                    goAppLogin();

                } else {
                    hideProgressDialog();
                    Toast.makeText(this, "Google Login war nicht erfolgreich", Toast.LENGTH_SHORT).show();
                    // Todo Signed out show unauthenticated UI.
                    Log.d("LoginActivity", "Sign In was not successfull");
                    Log.e("LoginActivity", "Sign In was not successfull");

                }
                break;

            case RC_REGISTER:
                GoogleSignInResult resultRegist = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                if (resultRegist.isSuccess()) {
                    GoogleSignInAccount acct = resultRegist.getSignInAccount();
                    // Get account information
                    userFullName = acct.getDisplayName();
                    userIdToken = acct.getIdToken();

                    goAppRegister();
                } else {
                    Toast.makeText(this, "Google Login war nicht erfolgreich", Toast.LENGTH_SHORT).show();
                    hideProgressDialog();
                    Log.d("LoginActivity", "Register was not successful" +  resultRegist.getStatus().toString());
                }
                break;
            default:
                hideProgressDialog();
                Toast.makeText(this, "Google: Wrong Request Code", Toast.LENGTH_SHORT).show();
        }
    }
        // [END onActivityResult]


    /**
     * Starte ein Login Service für die GoApp
     */
    public void goAppLogin() {
        // Activity still blockt from Blocker nr. 12,7

        Intent loginIntent = new Intent(this, LoginService.class);

        activityReceiver = new ServiceResultReceiver(new Handler());
        activityReceiver.setReceiver(this);
        loginIntent.putExtra(CommunicationKeys.RECEICER, activityReceiver);
        loginIntent.putExtra(CommunicationKeys.COMMAND, CommunicationKeys.PUT);
        loginIntent.putExtra(CommunicationKeys.USER_ID_TOKEN, userIdToken);
        startService(loginIntent);
    }

    /**
     * Startet ein Login Service um sich zu registiern
     */
    public void goAppRegister() {

        Intent registerIntent = new Intent(this, LoginService.class);
        activityReceiver = new ServiceResultReceiver(new Handler());
        activityReceiver.setReceiver(this);
        registerIntent.putExtra(CommunicationKeys.RECEICER, activityReceiver);


        // todo needed for a Test ------------------------------------------------------------------------------
        //fails testGoAppRegister in LoginActivityTest
        User user = new User(-1, newUserName.getText().toString());
        String jUser = userConvert.serialize(user, User.class);
        registerIntent.putExtra(CommunicationKeys.USER, jUser);
        //todo ----------------------------------------------------------------------------------------------

        // Post = Register this User
        registerIntent.putExtra(CommunicationKeys.COMMAND, CommunicationKeys.POST);
        registerIntent.putExtra(CommunicationKeys.USER_ID_TOKEN, userIdToken);
        startService(registerIntent);

        /*
        String jUser = userConvert.serialize(user, User.class);

        activityReceiver = new ServiceResultReceiver(new Handler());
        activityReceiver.setReceiver(this);

        Intent newUserIntent = new Intent(this, UserService.class);
        newUserIntent.putExtra(CommunicationKeys.RECEICER, activityReceiver);

        // Post = Add a New user
        newUserIntent.putExtra(CommunicationKeys.COMMAND, CommunicationKeys.POST);
        newUserIntent.putExtra(CommunicationKeys.USER, jUser);
        newUserIntent.putExtra(CommunicationKeys.GOOGLE_ID, googleId);
        */
    }

    /**
     * show if a Progress Dialog doesn't exist
     */
    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("Lädt...");
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    /**
     * hide a existing Progress Dialog
     */
    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }


    /**
     * Händelt alle Service Ergebnis
     * @param resultCode
     * @param resultData
     */
    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {

        switch (resultCode) {
            case 200:

                // TODO delet this. it is only for testing
                Toast.makeText(this, "Result code 200 !Test!", Toast.LENGTH_SHORT).show();

                switch (resultData.getString(CommunicationKeys.SERVICE)) {

                    case CommunicationKeys.FROM_LOGIN_SERVICE:
                        loginResultHandler(resultData);
                        break;

                    case CommunicationKeys.FROM_USER_SERVICE:
                        userResultHandler(resultData);
                        break;

                 }
                break;
            case 400:
                hideProgressDialog();
                Toast.makeText(this, "Error 400: Bad request", Toast.LENGTH_LONG).show();
                break;
            case 403:
                hideProgressDialog();
                Toast.makeText(this, "Error 403: forbidden request", Toast.LENGTH_LONG).show();
                break;
            case 408:
                hideProgressDialog();
                Toast.makeText(this, "Error 408: time out", Toast.LENGTH_LONG).show();
                break;
            case 500:
                hideProgressDialog();
                Toast.makeText(this, "Error 500: unexpected Error", Toast.LENGTH_LONG).show();
                break;
            default:
                hideProgressDialog();
                Toast.makeText(this,""+ resultCode,Toast.LENGTH_LONG).show();

        }
    }

    /**
     * UserService result handler
     * @param resultData result data
     */
    private void userResultHandler(Bundle resultData) {
        switch (resultData.getString(CommunicationKeys.COMMAND)) {
            case CommunicationKeys.GET:
                // dont neet it in this activity
                hideProgressDialog();
                // todo Error Wrong Command
                break;
            case CommunicationKeys.POST:
                // a new User has registered (Added a User)

                Toast.makeText(this, "Willkommen! Sie sind Registiert", Toast.LENGTH_SHORT).show();

                goAppLogin();

                break;

            case CommunicationKeys.PUT:
                hideProgressDialog();
                break;

            default:
                hideProgressDialog();
                Toast.makeText(this, "Error: 500 wrong Command", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Login Service result handler
     * @param resultData
     */
    private void loginResultHandler(Bundle resultData) {

        switch (resultData.getString(CommunicationKeys.COMMAND)) {
            case CommunicationKeys.GET:
                hideProgressDialog();
                // need it ?
                break;
            case CommunicationKeys.POST:
                // a new User has registered

                User newUser = new User(-1, newUserName.getText().toString());

                startnewUserService(newUser);

                break;

            case CommunicationKeys.PUT:

                String jUser = resultData.getString(CommunicationKeys.USER);

                User myUser = userConvert.deserialize(jUser, User.class);

                Toast.makeText(this, "Sie sind eingelogt", Toast.LENGTH_SHORT).show();

                // sharePreferances save name and Password
                SharedPreferences sharedPreferences = getSharedPreferences("UserData", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("userName", myUser.getName());
                editor.putInt("userId", myUser.getId());

                editor.commit();

                hideProgressDialog();

                // TODO Permissions

                getPermissions();

                // TODO zum testen, delete this ---------------------->
                // MeetingListActivity.start(this);


                break;
            default:
                hideProgressDialog();
                // TODO wrong Command
                Toast.makeText(this, "Error: 500 wrong Command", Toast.LENGTH_SHORT).show();
        }

    }

    private void getPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.ACCESS_COARSE_LOCATION },
                    PERMISSION_ACCESS_COARSE_LOCATION);
            Log.d("Made","acked for promissions");
        } else {
            Log.d("Made","did not acked for promissions");

            MeetingListActivity.start(this);
            // Todo Put it Not in the Stack
        }
    }

    /**
     *
     * @param user
     */
    private void startnewUserService(User user) {
        // Activity still blockt from Blocker nr. 42

        String jUser = userConvert.serialize(user, User.class);

        activityReceiver = new ServiceResultReceiver(new Handler());
        activityReceiver.setReceiver(this);

        Intent newUserIntent = new Intent(this, UserService.class);
        newUserIntent.putExtra(CommunicationKeys.RECEICER, activityReceiver);

        // Post = Add a New user
        newUserIntent.putExtra(CommunicationKeys.COMMAND, CommunicationKeys.POST);
        newUserIntent.putExtra(CommunicationKeys.USER, jUser);
        newUserIntent.putExtra(CommunicationKeys.GOOGLE_ID, googleId);
        startService(newUserIntent);
    }

    public void cheateToMain(View view) {

        User myUser = new User(10, "Aff forever and ever <3ah oah");
        // sharePreferances save name and Password
        SharedPreferences sharedPreferences = getSharedPreferences("UserData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("userName", myUser.getName());
        editor.putInt("userId", myUser.getId());

        editor.commit();

        MeetingListActivity.start(this);
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
                Log.d("Made","granted");
            } catch (final SecurityException ex) {
            }
            // permission was granted, yay!
            //googleApiClient = new GoogleApiClient.Builder(this, this, this).addApi(LocationServices.API).build();
            MeetingListActivity.start(this);
        } else {

            // permission denied, boo!
        }
        return;
    }

    /**
     * Bei Gooogle Connection fail create a Log
     * @param connectionResult connection result
     */
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }
}
