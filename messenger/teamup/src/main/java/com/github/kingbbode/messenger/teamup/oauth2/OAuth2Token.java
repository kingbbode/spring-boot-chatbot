package com.github.kingbbode.messenger.teamup.oauth2;

import java.util.Date;

/**
 * Created by YG on 2017-02-07.
 */
public class OAuth2Token {

    public OAuth2Token() {
    }

    public OAuth2Token(String access_token, long expires_in, String token_type, String refresh_token){
        this.access_token = access_token;
        this.expires_in = expires_in;
        this.token_type = token_type;
        this.refresh_token = refresh_token;
    }

    private String access_token;

    private long expires_in;

    private String token_type;

    private String refresh_token;

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public void setExpires_in(long expires_in) {
        this.expires_in = System.currentTimeMillis() + expires_in;
    }

    public void setToken_type(String token_type) {
        this.token_type = token_type;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }

    public String getAccessToken() {
        return token_type + " " + access_token;
    }

    public String getRefreshToken() {
        return refresh_token;
    }

    public boolean isExpired(){
        return new Date(this.expires_in).before(new Date());
    }
}
