package com.github.kingbbode.chatbot.autoconfigure.messenger.teamup;

import com.github.kingbbode.chatbot.core.brain.DispatcherBrain;
import com.github.kingbbode.chatbot.core.brain.factory.BrainFactoryCustomizer;
import com.github.kingbbode.chatbot.core.common.properties.BotProperties;
import com.github.kingbbode.chatbot.core.common.util.RestTemplateFactory;
import com.github.kingbbode.chatbot.core.event.EventQueue;
import com.github.kingbbode.messenger.teamup.*;
import com.github.kingbbode.messenger.teamup.message.MessageService;
import com.github.kingbbode.messenger.teamup.templates.template.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

import static com.github.kingbbode.chatbot.core.common.util.RestTemplateFactory.getRestOperations;

/**
 * Created by YG on 2017-07-10.
 */
@Configuration
@ConditionalOnClass(name = "com.github.kingbbode.messenger.teamup.TeamUpDispatcher")
public class TeamUpAutoConfiguration {

    public static final String EVENT_REST_OPERATIONS = "eventRestOperations";
    public static final String FILE_REST_OPERATIONS = "fileRestOperations";

    @Bean(name = FILE_REST_OPERATIONS)
    public RestOperations fileRestOperations() {
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setConnectTimeout(20000);
        factory.setReadTimeout(20000);
        RestTemplate restTemplate = (RestTemplate) RestTemplateFactory.getRestOperations(factory);
        restTemplate.getMessageConverters().add(new ByteArrayHttpMessageConverter());
        return restTemplate;
    }

    @Bean(name = EVENT_REST_OPERATIONS)
    public RestOperations eventRestOperations() {
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setConnectTimeout(30000);
        factory.setReadTimeout(30000);
        return getRestOperations(factory);
    }

    @EnableScheduling
    @EnableConfigurationProperties({TeamUpProperties.class})
    public static class TeamUpConfiguration {

        @Bean
        @ConditionalOnMissingBean
        public Oauth2Template oauth2Template(RestTemplate restTemplate, TeamUpProperties teamUpProperties){
            return new Oauth2Template(restTemplate, teamUpProperties);
        }

        @Bean
        @ConditionalOnMissingBean
        public TeamUpEventSensor teamUpEventSensor(EventQueue eventQueue){
            return new TeamUpEventSensor(eventTemplate(null), eventQueue, teamUpDispatcher(null, null));
        }

        @Bean
        @ConditionalOnMissingBean
        public TeamUpTokenManager teamUpTokenManager(){
            return new TeamUpTokenManager(teamUpEventSensor(null), oauth2Template(null, null));
        }

        @Bean
        @ConditionalOnMissingBean
        public AuthTemplate authTemplate(RestTemplate restTemplate){
            return new AuthTemplate(teamUpTokenManager(), restTemplate);
        }

        @Bean
        @ConditionalOnMissingBean
        public EdgeTemplate edgeTemplate(TeamUpProperties teamUpProperties, BotProperties botProperties, RestTemplate restTemplate){
            return new EdgeTemplate(teamUpTokenManager(), restTemplate, botProperties, teamUpProperties);
        }

        @Bean
        @ConditionalOnMissingBean
        public EventTemplate eventTemplate(@Qualifier(value = EVENT_REST_OPERATIONS) RestTemplate restTemplate){
            return new EventTemplate(teamUpTokenManager(), restTemplate);
        }

        @Bean
        @ConditionalOnMissingBean
        public FileTemplate fileTemplate(@Qualifier(value = FILE_REST_OPERATIONS) RestTemplate restTemplate){
            return new FileTemplate(restTemplate, teamUpTokenManager());
        }

        @Bean
        @ConditionalOnMissingBean
        public TeamUpDispatcher teamUpDispatcher(DispatcherBrain dispatcherBrain, TeamUpProperties teamUpProperties){
            return new TeamUpDispatcher(messageService(), dispatcherBrain, teamUpProperties);
        }

        @Bean
        @ConditionalOnMissingBean
        public MessageService messageService(){
            return new MessageService(edgeTemplate(null, null, null), fileTemplate(null));
        }

        @Bean
        @ConditionalOnMissingBean
        public TeamUpMemberCached teamUpMemberCached(){
            return new TeamUpMemberCached();
        }

        @Bean
        @ConditionalOnMissingBean
        public TeamUpMemberService teamUpMemberService(){
            return new TeamUpMemberService(teamUpMemberCached(), authTemplate(null));
        }

    }

    @Bean
    public BrainFactoryCustomizer brainFactoryCustomizer() {
        return () -> Collections.singletonList("com.github.kingbbode.messenger.teamup.brain");
    }
}
