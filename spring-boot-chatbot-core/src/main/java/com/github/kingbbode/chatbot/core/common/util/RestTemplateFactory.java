package com.github.kingbbode.chatbot.core.common.util;

import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by YG on 2017-01-20.
 */
public class RestTemplateFactory {
    public static RestOperations getRestOperations(HttpComponentsClientHttpRequestFactory factory) {
        RestTemplate restTemplate = new RestTemplate(factory);

        StringHttpMessageConverter stringMessageConverter = new StringHttpMessageConverter(Charset.forName("UTF-8"));
        MappingJackson2HttpMessageConverter jackson2Converter = new MappingJackson2HttpMessageConverter();
        ByteArrayHttpMessageConverter byteArrayHttpMessageConverter = new ByteArrayHttpMessageConverter();
        FormHttpMessageConverter formHttpMessageConverter = new FormHttpMessageConverter();
        formHttpMessageConverter.setCharset(Charset.forName("UTF-8"));

        List<HttpMessageConverter<?>> converters = new ArrayList<>();
        converters.add(jackson2Converter);
        converters.add(stringMessageConverter);
        converters.add(byteArrayHttpMessageConverter);
        converters.add(formHttpMessageConverter);

        restTemplate.setMessageConverters(converters);
        return restTemplate;
    }
}
