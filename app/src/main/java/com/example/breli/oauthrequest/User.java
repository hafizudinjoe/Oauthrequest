package com.example.breli.oauthrequest;

/**
 * Created by gmetax on 2/2/2017.
 */

public class User {
    private long id;
    private String token;
    private String secret;
    private String userid;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getUserId() {
        return userid;
    }

    public void setUserId(String userid){
        this.userid = userid;
    }

    @Override
    public String toString() {
        return "userId = "+userid + " token : "+token +" secret : "+ secret;
    }
}
