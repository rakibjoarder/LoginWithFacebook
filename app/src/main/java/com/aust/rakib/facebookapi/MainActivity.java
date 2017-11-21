package com.aust.rakib.facebookapi;
//https://www.youtube.com/watch?v=MPSJ1Qo_ZxQ&t=397s
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;


import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import org.json.JSONException;
import org.json.JSONObject;



public class MainActivity extends AppCompatActivity {
    private CallbackManager callbackManager;
    private String first_name,last_name,name,id,gender;
    private AccessToken accessToken;
    private LoginButton loginButton;
    AccessTokenTracker accessTokenTracker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        accessToken = AccessToken.getCurrentAccessToken();
        loginButton = (LoginButton)findViewById(R.id.login_button);

        loginButton.setReadPermissions("email","public_profile");
        callbackManager = CallbackManager.Factory.create();

        if(accessToken != null)
        {
            loginButton.setVisibility(View.GONE);
        }


        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                String userid=loginResult.getAccessToken().getUserId();
                Log.e("Tosdak",loginResult.getAccessToken()+"");
                GraphRequest graphRequest=GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        displayUserinfo(object);
                        loginButton.setVisibility(View.GONE);
                    }
                });
                Bundle parameters = new Bundle();
                parameters.putString("fields","first_name,last_name,name,id,gender");
                graphRequest.setParameters(parameters);
                graphRequest.executeAsync();
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });
    }

    private void displayUserinfo(JSONObject object) {
        try {
          if(object !=null)
          {
              first_name=object.getString("first_name");
              last_name=object.getString("last_name");
              name=object.getString("name");
              id=object.getString("id");
              gender=object.getString("gender");
          }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Intent main = new Intent( MainActivity.this, SecondActivity.class );
        main.putExtra( "name", name );
        main.putExtra( "id", id );
        main.putExtra( "first_name", first_name );
        main.putExtra( "gender", gender );
        startActivity( main );
        finish();


    }


    @Override
    protected void onResume() {
        super.onResume();

       accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
            }
        };

        if (accessToken != null) {
            GraphRequest graphRequest=GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
                @Override
                public void onCompleted(JSONObject object, GraphResponse response) {
                    displayUserinfo(object);
                }
            });
            Bundle parameters = new Bundle();
            parameters.putString("fields","first_name,last_name,name,id,gender");
            graphRequest.setParameters(parameters);
            graphRequest.executeAsync();

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        accessTokenTracker.stopTracking();
    }
}



//to get sha key   (in oncreate)
//        try {
//            PackageInfo info = getPackageManager().getPackageInfo(
//                    "com.aust.rakib.facebookapi",
//                    PackageManager.GET_SIGNATURES);
//            for (Signature signature : info.signatures) {
//                MessageDigest md = MessageDigest.getInstance("SHA");
//                md.update(signature.toByteArray());
//                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
//            }
//        } catch (PackageManager.NameNotFoundException e) {
//
//        } catch (NoSuchAlgorithmException e) {
//
//        }