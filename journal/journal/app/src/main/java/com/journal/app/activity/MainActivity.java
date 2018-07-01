/*
 * Copyright 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.journal.app.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.journal.app.R;

import cn.pedant.SweetAlert.SweetAlertDialog;

// Importing Google GMS Auth API Libraries.

public class MainActivity extends AppCompatActivity {

    // TAG is for show some tag logs in LOG screen.
    public static final String TAG = "ToDoMainActivity";

    // Request sing in code. Could be anything as you required.
    public static final int RequestSignInCode = 7;

    // Firebase Auth Object.
    public FirebaseAuth firebaseAuth;

    // Google API Client object.
    public GoogleApiClient googleApiClient;

    // Sing out button.
    TextView SignOutButton;

    // Google Sign In button .
    com.google.android.gms.common.SignInButton signInButton;
    //TextView signInButton;
    LinearLayout normal_login;

    // TextView to Show Login User Email and Name.
    TextView username, pin;String response="login";
    ProgressDialog progressDialog = null;
    public MainActivity()
    {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in);
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Please Wait...");
        Intent in = getIntent();
        /*if(!in.equals(null))
        {
         response = in.getStringExtra("data");
         //System.out.println("<<<<<<<<<<<<<<<<<<<<>>>>>>>>>>>>>>>>>"+response);
         if(response!=null&&response.equals("yes"))
         {
         UserSignOutFunction();
             //System.out.println("<<<<<<<<<<<<<<<<<<<<>>>>>>>>>>>>>>>>>"+response);
         }
        }*/

        // Getting Firebase Auth Instance into firebaseAuth object.
        firebaseAuth = FirebaseAuth.getInstance();
        signInButton = (com.google.android.gms.common.SignInButton) findViewById(R.id.signinbutton);
        normal_login=(LinearLayout) findViewById(R.id.noramal);
        SignOutButton= (TextView) findViewById(R.id.sign_out);

        username = (TextView) findViewById(R.id.user_name);
        username.setText("Test");
        pin = (TextView) findViewById(R.id.pin);
        pin.setText("Test123");
        signInButton = (com.google.android.gms.common.SignInButton) findViewById(R.id.signinbutton);



        // Creating and Configuring Google Sign In object.
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(getString(R.string.default_web_client_id))
        .requestEmail()
        .build();

        // Creating and Configuring Google Api Client.
        googleApiClient = new GoogleApiClient.Builder(MainActivity.this)
        .enableAutoManage(MainActivity.this , new GoogleApiClient.OnConnectionFailedListener() {
        @Override
        public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

        }
        } /* OnConnectionFailedListener */)
        .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
        .build();


// Adding Click listener to User normal sign in.
        normal_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isConnectingToInternet())
                {
                String usr="Test";
                String pn="Test123";
                String user=username.getText().toString();
                String userpin=pin.getText().toString();

                if(user.equals(usr) && userpin.equals(pn))
                {
                opentodoactivity();
                }
                else
                {
                new SweetAlertDialog(MainActivity.this, SweetAlertDialog.ERROR_TYPE)
                .setTitleText("Login Failed")
                .setContentText("Please enter the correct username and pin")
                .show();
                }

                }

                else
                {
                Toast.makeText(MainActivity.this,"Please activate your internet",Toast.LENGTH_LONG).show();
                }
            }
        });

        // Adding Click listener to User Sign in Google button.
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isConnectingToInternet())
                {
                    progressDialog.show();
                    UserSignInMethod();
                }
                else
                {
                Toast.makeText(MainActivity.this,"Please activate your internet",Toast.LENGTH_LONG).show();
                }
            }
        });

        // Adding Click Listener to User Sign Out button.
        SignOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                UserSignOutFunction();

            }
        });

    }


    // Sign In function Starts From Here.
    public void UserSignInMethod()
    {

    // Passing Google Api Client into Intent.
    Intent AuthIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);

    startActivityForResult(AuthIntent, RequestSignInCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

    super.onActivityResult(requestCode, resultCode, data);

    if (requestCode == RequestSignInCode){

    GoogleSignInResult googleSignInResult = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
    if (googleSignInResult.isSuccess())
    {
    //System.out.println("----------success----------------");
    GoogleSignInAccount googleSignInAccount = googleSignInResult.getSignInAccount();

    FirebaseUserAuth(googleSignInAccount);
    }

    }
    }

    public void FirebaseUserAuth(GoogleSignInAccount googleSignInAccount) {
        AuthCredential authCredential = GoogleAuthProvider.getCredential(googleSignInAccount.getIdToken(), null);

        //Toast.makeText(MainActivity.this,""+ authCredential.getProvider(),Toast.LENGTH_LONG).show();

        firebaseAuth.signInWithCredential(authCredential)
        .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> AuthResultTask) {

        if (AuthResultTask.isSuccessful()){
            progressDialog.dismiss();
            opentodoactivity();

       /* // Getting Current Login user details.
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
        */
        }
        else
        {
            Toast.makeText(MainActivity.this,"Something Went Wrong",Toast.LENGTH_LONG).show();
        }
        }
        });
    }

    public void UserSignOutFunction() {

        // Sing Out the User.
        FirebaseAuth firebaseAuth=  FirebaseAuth.getInstance();
        firebaseAuth.signOut();

        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(
        new ResultCallback<Status>() {
        @Override
        public void onResult(@NonNull Status status) {

        // Write down your any code here which you want to execute After Sign Out.

        // Printing Logout toast message on screen.
        Toast.makeText(MainActivity.this, "Logout Successfully", Toast.LENGTH_LONG).show();

        }
        });

       /* // After logout Hiding sign out button.
        SignOutButton.setVisibility(View.GONE);

        // After logout setting up email and name to null.
        LoginUserName.setText(null);
        LoginUserEmail.setText(null);

        // After logout setting up login button visibility to visible.
        signInButton.setVisibility(View.VISIBLE);*/
       }
       public void opentodoactivity()
       {
         Intent intent = new Intent(this, ToDoMainActivity.class);
         startActivity(intent);
         finish();
       }

       //Check internet connection
    public boolean isConnectingToInternet()
    {
    ConnectivityManager connectivity = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
    if (connectivity != null)
    {
    NetworkInfo[] info = connectivity.getAllNetworkInfo();
    if (info != null)
    for (int i = 0; i < info.length; i++)
    if (info[i].getState() == NetworkInfo.State.CONNECTED)
    {
        return true;
    }

    }
    return false;
    }


}