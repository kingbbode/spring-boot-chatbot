package com.github.kingbbode.chatbot.autoconfigure.messenger.teamup;

import com.github.kingbbode.chatbot.core.brain.factory.BrainFactoryCustomizer;
import com.github.kingbbode.messenger.teamup.*;
import com.github.kingbbode.messenger.teamup.message.MessageService;
import com.github.kingbbode.messenger.teamup.templates.template.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.Collections;

/**
 * Created by YG on 2017-07-10.
 */
@Configuration
@ConditionalOnClass(name = "com.github.kingbbode.messenger.teamup.TeamUpDispatcher")
public class TeamUpAutoConfiguration {

    @EnableScheduling
    @EnableConfigurationProperties({TeamUpProperties.class})
    public static class TeamUpConfiguration {
        @Bean
        @ConditionalOnMissingBean
        public TeamUpEventSensor teamUpEventSensor(){
            return new TeamUpEventSensor();
        }

        @Bean
        @ConditionalOnMissingBean
        public TeamUpTokenManager teamUpTokenManager(){
            return new TeamUpTokenManager();
        }

        @Bean
        @ConditionalOnMissingBean
        public TeamUpDispatcher teamUpDispatcher(){
            return new TeamUpDispatcher();
        }

        @Bean
        @ConditionalOnMissingBean
        public MessageService messageService(){
            return new MessageService();
        }

        @Bean
        @ConditionalOnMissingBean
        public Oauth2Template oauth2Template(){
            return new Oauth2Template();
        }

        @Bean
        @ConditionalOnMissingBean
        public AuthTemplate authTemplate(){
            return new AuthTemplate();
        }

        @Bean
        @ConditionalOnMissingBean
        public EdgeTemplate edgeTemplate(){
            return new EdgeTemplate();
        }

        @Bean
        @ConditionalOnMissingBean
        public EventTemplate eventTemplate(){
            return new EventTemplate();
        }

        @Bean
        @ConditionalOnMissingBean
        public FileTemplate fileTemplate(){
            return new FileTemplate();
        }

        @Bean
        @ConditionalOnMissingBean
        public TeamUpMemberCached teamUpMemberCached(){
            return new TeamUpMemberCached();
        }

        @Bean
        @ConditionalOnMissingBean
        public TeamUpMemberService teamUpMemberService(){
            return new TeamUpMemberService();
        }

    }

    @Bean
    public BrainFactoryCustomizer brainFactoryCustomizer() {
        return () -> Collections.singletonList("com.github.kingbbode.messenger.teamup.brain");
    }
}
