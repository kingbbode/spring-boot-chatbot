package com.github.kingbbode.messenger.teamup.templates.template;

import com.github.kingbbode.messenger.teamup.Api;
import com.github.kingbbode.messenger.teamup.TeamUpTokenManager;
import com.github.kingbbode.messenger.teamup.request.FileRequest;
import com.github.kingbbode.messenger.teamup.response.FileUploadResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestOperations;

import java.net.SocketTimeoutException;

/**
 * Created by YG on 2017-05-17.
 */
@Slf4j
public class FileTemplate {

    private final RestOperations restOperations;
    private final TeamUpTokenManager tokenManager;

    public FileTemplate(RestOperations restOperations, TeamUpTokenManager tokenManager) {
        this.restOperations = restOperations;
        this.tokenManager = tokenManager;
    }

    public byte[] download(FileRequest fileRequest) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", tokenManager.getAccessToken());
        HttpEntity<Object> entity = new HttpEntity<>(null, headers);
        ResponseEntity<byte[]> responseEntity = null;

        try {
            responseEntity = restOperations.exchange(Api.FILE_DOWNLOAD.getUrl() + fileRequest.getParam(), HttpMethod.GET, entity, byte[].class);
        } catch (ResourceAccessException e) {
            Throwable t = e.getCause();
            if (t != null && !(t instanceof SocketTimeoutException)) {
                log.error("ResourceAccessException - {}", e);
            }
        }catch (HttpClientErrorException e){
            log.error("HttpClientErrorException - {}", e);
        } catch (RestClientException e) {
            log.error("download.." + fileRequest.getParam(), e);
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

    public FileUploadResponse upload(FileRequest fileRequest, byte[] bytes) {
        ByteArrayResource image = new    ByteArrayResource(bytes){
            @Override
            public String getFilename(){
                return fileRequest.getFileName();
            }
        };
        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.MULTIPART_FORM_DATA);
        header.add("Authorization", tokenManager.getAccessToken());

        MultiValueMap<String, Object> multipartRequest = new LinkedMultiValueMap<>();
        HttpHeaders imageHeader = new HttpHeaders();
        imageHeader.setContentType(fileRequest.getType());
        HttpEntity<ByteArrayResource> imagePart = new HttpEntity<>(image, imageHeader);
        multipartRequest.add("files[]", imagePart);
        HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<>(multipartRequest, header);
        ResponseEntity<FileUploadResponse> responseEntity = null;
        try {
            responseEntity = restOperations.exchange(Api.FILE_UPLOAD.getUrl() + fileRequest.getTeam(), HttpMethod.POST, entity, FileUploadResponse.class);
        } catch (ResourceAccessException e) {
            Throwable t = e.getCause();
            if (t != null && !(t instanceof SocketTimeoutException)) {
                log.error("ResourceAccessException - {}", e);
            }
        }catch (HttpClientErrorException e){
            log.error("HttpClientErrorException - {}", e);
        } catch (RestClientException e) {
            log.error("upload.." + fileRequest.getTeam(), e);
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
}
