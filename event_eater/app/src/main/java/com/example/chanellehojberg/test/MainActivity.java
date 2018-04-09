package com.example.chanellehojberg.test;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.AccessToken;
import org.json.JSONObject;

import java.util.Arrays;


public class MainActivity extends AppCompatActivity {

    TextView textStatus;
    LoginButton login_button;
    CallbackManager callbackManager;
    AccessToken accessToken;
    String first_name;
    String last_name;
    String email;
    String gender;
    String profileURL;
    private static final String EMAIL = "email";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("EventEater");

        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_main);
        initializeControls();
        loginWithFB();




    }




    private void initializeControls(){

        textStatus = (TextView) findViewById(R.id.textStatus);
        login_button = (LoginButton) findViewById(R.id.login_button);
        //login_button.setFragment(this);
        login_button.setReadPermissions(Arrays.asList("user_status"));


        callbackManager = CallbackManager.Factory.create();



        AccessTokenTracker accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(
                    AccessToken oldAccessToken,
                    AccessToken currentAccessToken) {
                // Set the access token using
                // currentAccessToken when it's loaded or set.
            }
        };
        // If the access token is available already assign it.
        accessToken = AccessToken.getCurrentAccessToken();

        ProfileTracker profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(
                    Profile oldProfile,
                    Profile currentProfile) {
                // App code
            }
        };


    }




    private void loginWithFB(){
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                textStatus.setText("Login Success\n" + loginResult.getAccessToken());

                boolean loggedIn = AccessToken.getCurrentAccessToken() == null;
                //LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"));

                GraphRequest request = GraphRequest.newMeRequest(
                        accessToken,
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(
                                    JSONObject object,
                                    GraphResponse response)
                            {
                                Log.d("FB", "complete");
                                Log.d("FB", object.optString("name"));
                                Log.d("FB", object.optString("link"));
                                Log.d("FB", object.optString("id"));



                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,link");
                request.setParameters(parameters);
                request.executeAsync();



                Intent intent = new Intent(MainActivity.this, Main2Activity.class);
                startActivity(intent);



            }

            @Override
            public void onCancel() {
                textStatus.setText("Login Canceled");
            }

            @Override
            public void onError(FacebookException error) {
                textStatus.setText("Login Error: " + error.getMessage());

            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }
}
