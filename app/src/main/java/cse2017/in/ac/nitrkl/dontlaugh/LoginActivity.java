package cse2017.in.ac.nitrkl.dontlaugh;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.people.v1.People;
import com.google.api.services.people.v1.model.Date;
import com.google.api.services.people.v1.model.Gender;
import com.google.api.services.people.v1.model.Person;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserInfo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cse2017.in.ac.nitrkl.dontlaugh.SQLite.DontLaughDBHelper;

/**
 * Created by LENOVO on 26-10-2017.
 */

public class LoginActivity extends AppCompatActivity {
    // TAG is for show some tag logs in LOG screen.
    public static final String TAG = "MainActivity";

    // Request sing in code. Could be anything as you required.
    public static final int RequestSignInCode = 7;

    // Firebase Auth Object.
    public FirebaseAuth firebaseAuth;

    // Google API Client object.
    public GoogleApiClient googleApiClient;

    // Sing out button.
    Button SignOutButton;

    // Google Sign In button .
    com.google.android.gms.common.SignInButton signInButton;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    public static final int DEFAULT = 0;

    // TextView to Show Login User Email and Name.
    TextView LoginUserName, LoginUserEmail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        sharedPreferences = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        int status = sharedPreferences.getInt("status", DEFAULT);

        //Database Creation
        DontLaughDBHelper dbHelper = new DontLaughDBHelper(this);
        dbHelper.getWritableDatabase();


        if (status==1) {
            Toast.makeText(getApplicationContext(),"Status",Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        setContentView(R.layout.login);
        signInButton = (SignInButton) findViewById(R.id.sign_in_button);

        SignOutButton= (Button) findViewById(R.id.sign_out);

        LoginUserName = (TextView) findViewById(R.id.textViewName);

        LoginUserEmail = (TextView) findViewById(R.id.textViewEmail);

        signInButton = (com.google.android.gms.common.SignInButton)findViewById(R.id.sign_in_button);

        // Getting Firebase Auth Instance into firebaseAuth object.
        firebaseAuth = FirebaseAuth.getInstance();

        // Hiding the TextView on activity start up time.
        LoginUserEmail.setVisibility(View.INVISIBLE);
        LoginUserName.setVisibility(View.INVISIBLE);

        // Creating and Configuring Google Sign In object.
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestIdToken(getString(R.string.server_client_id))
                .requestServerAuthCode(getString(R.string.server_client_id))
                .requestScopes(new Scope("profile"))
                .build();

        // Build a GoogleApiClient with access to GoogleSignIn.API and the options above.
        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Log.d(TAG, "onConnectionFailed: ");
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
                .build();

     /*   GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        // Creating and Configuring Google Api Client.
        googleApiClient = new GoogleApiClient.Builder(LoginActivity.this)
                .enableAutoManage(LoginActivity.this , new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

                    }
                } *//* OnConnectionFailedListener *//*)
                .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
                .build();*/


        // Adding Click listener to User Sign in Google button.
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                UserSignInMethod();

            }
        });

        // Adding Click Listener to User Sign Out button.
        SignOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //UserSignOutFunction();
                mainAct();

            }
        });

    }


    @Override
    protected void onPause() {
        super.onPause();

    }

    // Sign In function Starts From Here.
    public void UserSignInMethod(){

        // Passing Google Api Client into Intent.
        Intent AuthIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        Log.i("googleApi","googleApi");
        startActivityForResult(AuthIntent, RequestSignInCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RequestSignInCode){

            GoogleSignInResult googleSignInResult = Auth.GoogleSignInApi.getSignInResultFromIntent(data);

            Log.i("req","req");

            if (googleSignInResult.isSuccess()){
                Log.i("reqSucess","reqSuccess");
                GoogleSignInAccount googleSignInAccount = googleSignInResult.getSignInAccount();
                googleAdditionalDetailsResult(data);

                FirebaseUserAuth(googleSignInAccount);
            }
            else
            {
                Toast.makeText(this,googleSignInResult.getStatus()+" ", Toast.LENGTH_LONG).show();
            }

        }

    }

    public void FirebaseUserAuth(GoogleSignInAccount googleSignInAccount) {

        AuthCredential authCredential = GoogleAuthProvider.getCredential(googleSignInAccount.getIdToken(), null);

        Toast.makeText(LoginActivity.this,""+ authCredential.getProvider(),Toast.LENGTH_LONG).show();

        firebaseAuth.signInWithCredential(authCredential)
                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task AuthResultTask) {
                        Toast.makeText(LoginActivity.this,""+ AuthResultTask.toString(),Toast.LENGTH_LONG).show();
                        if (AuthResultTask.isSuccessful()){
                            startGoogleAdditionalRequest();

                            // Getting Current Login user details.
                            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

                            // Showing Log out button.
                            SignOutButton.setVisibility(View.VISIBLE);


                            // Hiding Login in button.
                            signInButton.setVisibility(View.GONE);

                            // Showing the TextView.
                            LoginUserEmail.setVisibility(View.VISIBLE);
                            LoginUserName.setVisibility(View.VISIBLE);

                            // Setting up name into TextView.
                            LoginUserName.setText("NAME =  "+ firebaseUser.getDisplayName().toString());

                            // Setting up Email into TextView.
                            LoginUserEmail.setText("Email =  "+ firebaseUser.getEmail().toString());
                            editor.putInt("status",1);
                            editor.commit();
                            startGoogleAdditionalRequest();


                        }else {
                            Toast.makeText(LoginActivity.this,"Something Went Wrong",Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    public void UserSignOutFunction() {

        // Sing Out the User.
        firebaseAuth.signOut();

        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(
                new ResultCallback() {
                    @Override
                    public void onResult(@NonNull Result result) {
                        Toast.makeText(LoginActivity.this, "Logout Successfully", Toast.LENGTH_LONG).show();

                    }

                });

        // After logout Hiding sign out button.
        SignOutButton.setVisibility(View.GONE);

        // After logout setting up email and name to null.
        LoginUserName.setText(null);
        LoginUserEmail.setText(null);

        // After logout setting up login button visibility to visible.
        signInButton.setVisibility(View.VISIBLE);
    }
    public void mainAct(){
        Intent intent = new Intent(LoginActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }
    //-------------------------------------------------------------------------------------------
    private void setupGoogleAdditionalDetailsLogin() {
        // Configure sign-in to request the user's ID, email address, and basic profile. ID and
        // basic profile are included in DEFAULT_SIGN_IN.

    }

    public void googleAdditionalDetailsResult(Intent data) {
        Log.i(TAG, "googleAdditionalDetailsResult: ");
        GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
        if (result.isSuccess()) {
            // Signed in successfully
            GoogleSignInAccount acct = result.getSignInAccount();
            // execute AsyncTask to get data from Google People API
            new GoogleAdditionalDetailsTask().execute(acct);
        } else {
            Log.i(TAG, "googleAdditionalDetailsResult: fail");

        }
    }

    private void startGoogleAdditionalRequest() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(signInIntent, RequestSignInCode);
    }

    public class GoogleAdditionalDetailsTask extends AsyncTask<GoogleSignInAccount, Void, Person> {
        @Override
        protected Person doInBackground(GoogleSignInAccount... googleSignInAccounts) {
            Person profile = null;
            try {
                HttpTransport httpTransport = new NetHttpTransport();
                JacksonFactory jsonFactory = JacksonFactory.getDefaultInstance();

                //Redirect URL for web based applications.
                // Can be empty too.
                String redirectUrl = "urn:ietf:wg:oauth:2.0:oob";

                // Exchange auth code for access token
                GoogleTokenResponse tokenResponse = new GoogleAuthorizationCodeTokenRequest(
                        httpTransport,
                        jsonFactory,
                        getString(R.string.server_client_id),
                        getString(R.string.server_client_secret),
                        googleSignInAccounts[0].getServerAuthCode(),
                        redirectUrl
                ).execute();

                GoogleCredential credential = new GoogleCredential.Builder()
                        .setClientSecrets(getString(R.string.server_client_id),getString(R.string.server_client_secret))
                        .setTransport(httpTransport)
                        .setJsonFactory(jsonFactory)
                        .build();

                credential.setFromTokenResponse(tokenResponse);

                People peopleService = new People.Builder(httpTransport, jsonFactory, credential)
                                                .build();

                // Get the user's profile
                profile = peopleService.people().get("people/me").setRequestMaskIncludeField("person.names,person.genders").execute();
            } catch (IOException e) {
                Log.i(TAG, "doInBackground: " + e.getMessage());
                e.printStackTrace();
            }
            return profile;
        }
        String profileGender,profileBirthday,profileAbout,profileCover;
        @Override
        protected void onPostExecute(Person person) {
            if (person != null) {
                if (person.getGenders() != null && person.getGenders().size() > 0) {
                    profileGender = person.getGenders().get(0).getValue();
                }
                if (person.getBirthdays() != null && person.getBirthdays().get(0).size() > 0) {
//                    yyyy-MM-dd
                    Date dobDate = person.getBirthdays().get(0).getDate();
                    if (dobDate.getYear() != null) {
                        profileBirthday = dobDate.getYear() + "-" + dobDate.getMonth() + "-" + dobDate.getDay();
                    }
                }
                if (person.getBiographies() != null && person.getBiographies().size() > 0) {
                    profileAbout = person.getBiographies().get(0).getValue();
                }
                if (person.getCoverPhotos() != null && person.getCoverPhotos().size() > 0) {
                    profileCover = person.getCoverPhotos().get(0).getUrl();
                }
                Log.i(TAG, String.format("googleOnComplete: gender: %s, birthday: %s, about: %s, cover: %s", profileGender, profileBirthday, profileAbout, profileCover));
            }

        }
    }

}
