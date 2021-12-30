package com.github.kingbbode.chatbot.autoconfigure.messenger.slack;

import com.github.kingbbode.chatbot.core.brain.DispatcherBrain;
import com.github.kingbbode.chatbot.core.event.EventQueue;
import com.github.kingbbode.messenger.slack.SlackBotClient;
import com.github.kingbbode.messenger.slack.SlackDispatcher;
import com.github.kingbbode.messenger.slack.SlackEventSensor;
import com.github.kingbbode.messenger.slack.event.BlockActionDispatcherBrain;
import com.slack.api.bolt.App;
import com.slack.api.bolt.AppConfig;
import com.slack.api.bolt.socket_mode.SocketModeApp;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.websocket.DeploymentException;
import java.io.IOException;
import java.util.List;

@Configuration
@EnableConfigurationProperties(SlackProperties.class)
@ConditionalOnClass(name = "com.github.kingbbode.messenger.slack.SlackDispatcher")
@ConditionalOnProperty(prefix = "slack", value = {"app-token", "bot-token"})
public class SlackAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public App slackApp(SlackProperties slackProperties) {
        return new App(AppConfig.builder()
            .singleTeamBotToken(slackProperties.getBotToken())
            .build()
        );
    }

    @Bean
    @ConditionalOnMissingBean
    public SocketModeApp slackSocketModeApp(App app, SlackProperties slackProperties) throws IOException {
        return new SocketModeApp(slackProperties.getAppToken(), app);
    }

    @Bean
    @ConditionalOnMissingBean
    public SlackBotClient slackBotClient(App app, SlackProperties slackProperties) throws IOException, DeploymentException {
        return new SlackBotClient(app.slack().methods(slackProperties.getBotToken()));
    }

    @Bean
    @ConditionalOnMissingBean
    public SlackDispatcher slackDispatcher(SlackBotClient slackRTMClient) {
        return new SlackDispatcher(slackRTMClient);
    }

    @Bean
    @ConditionalOnMissingBean
    public SlackEventSensor slackEventSensor(
        App app,
        List<BlockActionDispatcherBrain> blockActionConnectors,
        SlackDispatcher slackDispatcher,
        EventQueue eventQueue,
        DispatcherBrain dispatcherBrain
    ) {
        return new SlackEventSensor(app, blockActionConnectors, slackDispatcher, eventQueue, dispatcherBrain);
    }
}
