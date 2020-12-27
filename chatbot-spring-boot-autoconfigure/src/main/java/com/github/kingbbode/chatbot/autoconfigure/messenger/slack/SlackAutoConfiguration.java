package com.github.kingbbode.chatbot.autoconfigure.messenger.slack;

import com.github.kingbbode.chatbot.core.event.EventQueue;
import com.github.kingbbode.messenger.slack.SlackDispatcher;
import com.github.kingbbode.messenger.slack.SlackEventSensor;
import com.github.kingbbode.messenger.slack.SlackRTMClient;
import com.slack.api.Slack;
import com.slack.api.rtm.RTMClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.websocket.DeploymentException;
import java.io.IOException;

@Configuration
@EnableConfigurationProperties(SlackProperties.class)
@ConditionalOnClass(name = "com.github.kingbbode.messenger.slack.SlackDispatcher")
@ConditionalOnProperty(prefix = "slack", value = "token")
public class SlackAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public SlackRTMClient slackRTMClient(SlackProperties slackProperties) throws IOException, DeploymentException {
        return new SlackRTMClient(slackProperties.getToken());
    }

    @Bean
    @ConditionalOnMissingBean
    public SlackDispatcher slackDispatcher(SlackRTMClient slackRTMClient) {
        return new SlackDispatcher(slackRTMClient);
    }

    @Bean
    @ConditionalOnMissingBean
    public SlackEventSensor slackEventSensor(SlackRTMClient slackRTMClient, SlackDispatcher slackDispatcher, EventQueue eventQueue) {
        return new SlackEventSensor(slackRTMClient, slackDispatcher, eventQueue);
    }
}
