package com.github.kingbbode.messenger.slack;

import allbegray.slack.SlackClientFactory;
import allbegray.slack.rtm.EventListener;
import allbegray.slack.rtm.SlackRealTimeMessagingClient;
import com.fasterxml.jackson.databind.JsonNode;
import com.github.kingbbode.chatbot.core.event.Event;
import com.github.kingbbode.chatbot.core.event.EventQueue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import javax.annotation.PostConstruct;
import java.util.Optional;

import static allbegray.slack.rtm.Event.MESSAGE;

@Slf4j
public class SlackEventSensor implements EventListener, InitializingBean, DisposableBean {
    private static final String BOT_MESSAGE = "bot_message";
    private static final String SUBTYPE = "subtype";
    private final SlackRealTimeMessagingClient slackRealTimeMessagingClient;
    private final SlackDispatcher slackDispatcher;
    private final EventQueue eventQueue;

    public SlackEventSensor(String token, SlackDispatcher slackDispatcher, EventQueue eventQueue) {
        this.slackRealTimeMessagingClient = SlackClientFactory.createSlackRealTimeMessagingClient(token, null, null);
        this.slackDispatcher = slackDispatcher;
        this.eventQueue = eventQueue;
    }

    @PostConstruct
    public void init() {
        this.slackRealTimeMessagingClient.addListener(MESSAGE, this);
        this.slackRealTimeMessagingClient.connect();
    }

    @Override
    public void onMessage(JsonNode message) {
        if(isBot(message)) {
            return;
        }
        this.eventQueue.offer(new Event<>(slackDispatcher, message));
    }

    private boolean isBot(JsonNode message) {
        return BOT_MESSAGE.equals(Optional.ofNullable(message.get(SUBTYPE)).map(JsonNode::asText).orElse(null));
    }

    @Override
    public void destroy() throws Exception {
        this.slackRealTimeMessagingClient.close();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("[BOT] Registered SlackDispatcher.");
    }
}
