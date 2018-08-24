package com.github.kingbbode.messenger.teamup.templates;

import com.github.kingbbode.messenger.teamup.TeamUpTokenManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestOperations;

import java.net.SocketTimeoutException;

/**
 * Created by YG on 2016-10-13.
 */
@Slf4j
@RequiredArgsConstructor
public class BaseTemplate {

    private final TeamUpTokenManager tokenManager;
    
    private final RestOperations restOperations;

    protected <T> T get(String url, ParameterizedTypeReference<T> p) {
        return send(url, null, p, HttpMethod.GET);
    }

    protected <T> T post(String url, Object request, ParameterizedTypeReference<T> p) {
        return send(url, request, p, HttpMethod.POST);
    }
    
    protected <T> T delete(String url, Object request, ParameterizedTypeReference<T> p) {
        return send(url, request, p, HttpMethod.DELETE);
    }
    
    private <T> T send(String url, Object request, ParameterizedTypeReference<T> p, HttpMethod httpMethod) {

        HttpEntity<Object> entity = getEntity(request);
        ResponseEntity<T> responseEntity = null;

        try {
            responseEntity = restOperations.exchange(url, httpMethod, entity, p);
        } catch (ResourceAccessException e) {
            Throwable t = e.getCause();
            if (t != null && !(t instanceof SocketTimeoutException)) {
                log.error("ResourceAccessException - {}", e);
            }
        }catch (HttpClientErrorException e){            
            log.error("HttpClientErrorException - {}", e);
        } catch (RestClientException e) {
            log.error(url, e);
        }
        catch (Exception e) {
            log.error("url", e);
        }

        if (responseEntity != null && responseEntity.getStatusCode().equals(HttpStatus.OK)) {
            return responseEntity.getBody();
        } else {
            if(responseEntity != null){
                log.error("StatusCode : " + responseEntity.getStatusCode());
            }
        }
        return null;
    }

    protected HttpEntity<Object> getEntity(Object request) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", tokenManager.getAccessToken());
        return new HttpEntity<>(request, headers);
    }
}
