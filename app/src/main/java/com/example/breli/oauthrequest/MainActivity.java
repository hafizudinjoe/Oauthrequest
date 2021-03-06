package com.example.breli.oauthrequest;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuth1RequestToken;
import com.github.scribejava.core.model.Token;
import com.github.scribejava.core.oauth.OAuth10aService;

import org.junit.rules.Verifier;
import java.util.List;

public class MainActivity extends AppCompatActivity{
    final int AUTHENTICATION_REQUEST = 101;
    final String LOGTAG = "WITHINGS";

    public static OAuth10aService service;
    //public static Token requestToken;
    public static OAuth1RequestToken requestToken;
    String secret, token;
    Token accessToken;
    String userId = "";

    private UsersDataSource datasource;
    private TextView nameTV;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nameTV = (TextView) findViewById(R.id.nameTitleTextView);
        nameTV.setText("--");

        getCredentials();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (requestCode == AUTHENTICATION_REQUEST) {

            if (resultCode == RESULT_OK) {
                Bundle extras = intent.getExtras();
                if (extras != null) {
                    userId = extras.getString("USERID");

                    getAccessTokenThread.execute((Object) null);
                }
            }
        }
    }
    @Override
    protected void onResume() {
        datasource.open();
        super.onResume();
    }

    @Override
    protected void onPause() {
        datasource.close();
        super.onPause();
    }

    private void getCredentials() {
        try {
            datasource = new UsersDataSource(this);
            datasource.open();

            List<User> users = datasource.getAllUsers();

            if (users.isEmpty()) {
                startAuthenticationActivity();
            } else {
                // TODO load all users and if isn't anyone correct
                // startAuthenticationActivity
                secret = users.get(0).getSecret();
                token = users.get(0).getToken();
                userId = users.get(0).getUserId();
                Log.i(LOGTAG, "secret  : " + secret);
                Log.i(LOGTAG, "token  : " + token);
                Log.i(LOGTAG, "userId  : " + userId);
                try {
                    service =  new ServiceBuilder()
                            .apiKey(WithingsApi.getKey())
                            .apiSecret(WithingsApi.getSecret())
                            .build(WithingsApi.instance());

                    requestToken = service.getRequestToken();
                    loadData();
                } catch (Exception ex) {
                    startAuthenticationActivity();
                }

            }
        } catch (Exception ex) {
            Log.e(LOGTAG, "try on create" + ex.getLocalizedMessage());
        }
    }

    private void startAuthenticationActivity() {
        Intent intent = new Intent(this, AuthenticationActivity.class);
        startActivityForResult(intent, AUTHENTICATION_REQUEST);
    }

    AsyncTask<Object, Object, Object> getAccessTokenThread = new AsyncTask<Object, Object, Object>() {
        @Override
        protected Object doInBackground(Object... params) {
            //https://github.com/scribejava/scribejava/wiki/Getting-Started#step-four-get-the-access-token

            //accessToken = service.getAccessToken(requestToken, new Verifier(""));       //no getAccessToken & cannot resolve verifier

            //secret = accessToken.getSecret();                                           //getSecret method is not exist
            //token = accessToken.getToken();                                             //getToken method is not exist
            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            // authentication complete send the token,secret,userid, to python
            datasource.createUser(token, secret, userId);
            loadData();                                                               //no loadData method. What to be loaded?
        }

    };


    private void loadData(){}

}
