package com.github.kingbbode.messenger.teamup.templates.template;

import com.github.kingbbode.chatbot.core.common.enums.GrantType;
import com.github.kingbbode.messenger.teamup.Api;
import com.github.kingbbode.messenger.teamup.TeamUpProperties;
import com.github.kingbbode.messenger.teamup.oauth2.OAuth2Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestOperations;

/**
 * Created by YG on 2016-10-14.
 */
public class Oauth2Template  {

    private final RestOperations restOperations;
    private final TeamUpProperties teamUpProperties;

    public Oauth2Template(RestOperations restOperations, TeamUpProperties teamUpProperties) {
        this.restOperations = restOperations;
        this.teamUpProperties = teamUpProperties;
    }

    public OAuth2Token token(OAuth2Token token){
        if (token == null) {
            return post(token, GrantType.PASSWORD);
        }else{
            if (token.isExpired()) {
                return refresh(token);
            }
        }
        return token;
    }

    private OAuth2Token refresh(OAuth2Token token) {
        return post(token, GrantType.REFRESH);
    }

    private OAuth2Token post(OAuth2Token token, GrantType grantType) {
        ResponseEntity<OAuth2Token> response = restOperations.postForEntity(Api.TOKEN.getUrl(), getEntity(token, grantType),
                OAuth2Token.class);

        if (response.getStatusCode().equals(HttpStatus.OK)) {
            token = response.getBody();
        }

        return token;
    }
    

    private HttpEntity<Object> getEntity(OAuth2Token token, GrantType grantType) {
        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, Object> data = new LinkedMultiValueMap<>();
        data.add("grant_type", grantType.getKey());
        if (GrantType.PASSWORD.equals(grantType)) {
            data.add("client_id", teamUpProperties.getClientId());
            data.add("client_secret", teamUpProperties.getClientSecret());
            data.add("username", teamUpProperties.getId());
            data.add("password", teamUpProperties.getPassword());
        } else if (GrantType.REFRESH.equals(grantType)) {
            data.add("refresh_token", token.getRefreshToken());
        }
        return new HttpEntity<>(data, header);
    }
}
