package com.example.breli.oauthrequest;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuth1RequestToken;
import com.github.scribejava.core.model.SignatureType;
import com.github.scribejava.core.oauth.OAuth10aService;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import static com.example.breli.oauthrequest.MainActivity.requestToken;


/**
 * Created by breli on 1/14/2017.
 */

@SuppressLint("SetJavaScriptEnabled")
public class AuthenticationActivity extends Activity {
    final String LOGTAG = "WITHINGS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);

        final WebView wvAuthorise = (WebView) findViewById(R.id.wvAuthorise);
        wvAuthorise.getSettings().setJavaScriptEnabled(true);
        wvAuthorise.setWebViewClient(new MyWebViewClient(wvAuthorise));

        final OAuth10aService service = new ServiceBuilder()                                //1. Create oauthservice object
                .apiKey(WithingsApi.getKey())
                .apiSecret(WithingsApi.getSecret())
                .signatureType(SignatureType.QueryString)
                .build(WithingsApi.instance());

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    requestToken = service.getRequestToken();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                final String authURL = service.getAuthorizationUrl(requestToken);
                wvAuthorise.post(new Runnable() {
                    @Override
                    public void run() {
                        wvAuthorise.loadUrl(authURL);
                    }
                });
            }
        }).start();

    }

    class MyWebViewClient extends WebViewClient{
        WebView wvAuthorise;
        MyWebViewClient(WebView wv){
            wvAuthorise = wv;
        }
        @Override
        public void onPageFinished(WebView view, String url) {
            getUSERID(url);
        }
    }

    private void getUSERID(final String url) {

        try {
            String divStr = "userid=";
            int first = url.indexOf(divStr);

            if(first!=-1){
                final String userid = url.substring(first+divStr.length());

                Intent intent = new Intent();
                intent.putExtra("USERID",userid);
                setResult(RESULT_OK,intent);
                finish();
            }
            else
            {
                //...
            }

        } catch (Exception e) {
            Log.e(LOGTAG,e.getMessage());
            //...
        }
    }

}