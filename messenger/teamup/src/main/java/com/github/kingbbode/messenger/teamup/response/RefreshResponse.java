package com.github.kingbbode.messenger.teamup.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by YG on 2016-03-28.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class RefreshResponse {
    private String access_token;
    private Integer expires_in;
    private String token_type;
    private String refresh_token;

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getToken_type() {
        return token_type;
    }

    public void setToken_type(String token_type) {
        this.token_type = token_type;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }

    public Integer getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(Integer expires_in) {
        this.expires_in = expires_in;
    }
}
