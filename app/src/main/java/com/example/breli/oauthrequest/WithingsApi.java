package com.example.breli.oauthrequest;


import com.github.scribejava.core.builder.api.DefaultApi10a;
import com.github.scribejava.core.model.OAuth1RequestToken;
import com.github.scribejava.core.model.OAuth1Token;
import com.github.scribejava.core.model.Token;

/**
 * Created by breli on 1/14/2017.
 */

public class WithingsApi extends DefaultApi10a {

    private static final String AUTHORIZATION_URL = "https://oauth.withings.com/account/authorize?oauth_token=";
    private static final String apiKey = "bf2034e484d2cc77dd9142955f31635d8d7b76919002d65f5c8c36ebd47";
    private static final String apiSecret = "292751d3722e5e9987905e17d4f13e9f9f12b9f3de2bde9c4300299701";

    protected WithingsApi() {
    }

    private static class InstanceHolder {
        private static final WithingsApi INSTANCE = new WithingsApi();
    }

    public static WithingsApi instance() {
        return InstanceHolder.INSTANCE;
    }

    @Override
    public String getRequestTokenEndpoint() {
        return "https://oauth.withings.com/account/request_token";
    }

    @Override
    public String getAccessTokenEndpoint() {
        return "https://oauth.withings.com/account/access_token";
    }

    @Override
    public String getAuthorizationUrl(OAuth1RequestToken requestToken) {
        return String.format(getAUTHORIZATION_URL(), requestToken.getToken());
    }

    public static String getKey() {
        return apiKey;
    }

    public static String getSecret() {
        return apiSecret;
    }

    public static String getAUTHORIZATION_URL() {
        return AUTHORIZATION_URL;
    }
}