package com.github.kingbbode.chatbot.autoconfigure.messenger.slack;

import com.github.kingbbode.chatbot.core.brain.DispatcherBrain;
import com.github.kingbbode.messenger.slack.SlackDispatcher;
import com.github.kingbbode.messenger.slack.SlackEventSensor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(SlackProperties.class)
@ConditionalOnClass(name = "com.github.kingbbode.messenger.slack.SlackDispatcher")
@ConditionalOnProperty(prefix = "slack", value = "token")
public class SlackAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public SlackDispatcher slackDispatcher(DispatcherBrain dispatcherBrain) {
        return new SlackDispatcher(dispatcherBrain);
    }

    @Bean
    @ConditionalOnMissingBean
    public SlackEventSensor lineEtSensor(SlackProperties slackProperties, SlackDispatcher slackDispatcher) {
        return new SlackEventSensor(slackProperties.getToken(), slackDispatcher);
    }
}
