package com.github.kingbbode.messenger.teamup.templates.template;

import com.github.kingbbode.messenger.teamup.Api;
import com.github.kingbbode.messenger.teamup.TeamUpTokenManager;
import com.github.kingbbode.messenger.teamup.request.FileRequest;
import com.github.kingbbode.messenger.teamup.response.FileUploadResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
public class FileTemplate {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    @Qualifier(value = "fileRestOperations")
    private RestOperations restOperations;


    @Autowired
    TeamUpTokenManager tokenManager;

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
                logger.error("ResourceAccessException - {}", e);
            }
        }catch (HttpClientErrorException e){
            logger.error("HttpClientErrorException - {}", e);
        } catch (RestClientException e) {
            logger.error("download.." + fileRequest.getParam(), e);
        }
        catch (Exception e) {
            logger.error("url", e);
        }

        if (responseEntity != null && responseEntity.getStatusCode().equals(HttpStatus.OK)) {
            return responseEntity.getBody();
        } else {
            if(responseEntity != null){
                logger.error("StatusCode : " + responseEntity.getStatusCode());
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
                logger.error("ResourceAccessException - {}", e);
            }
        }catch (HttpClientErrorException e){
            logger.error("HttpClientErrorException - {}", e);
        } catch (RestClientException e) {
            logger.error("upload.." + fileRequest.getTeam(), e);
        }
        catch (Exception e) {
            logger.error("url", e);
        }

        if (responseEntity != null && responseEntity.getStatusCode().equals(HttpStatus.OK)) {
            return responseEntity.getBody();
        } else {
            if(responseEntity != null){
                logger.error("StatusCode : " + responseEntity.getStatusCode());
            }
        }
        return null;
    }
}
