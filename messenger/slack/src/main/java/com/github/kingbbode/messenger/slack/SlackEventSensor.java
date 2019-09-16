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

import static allbegray.slack.rtm.Event.MESSAGE;

@Slf4j
public class SlackEventSensor implements EventListener, InitializingBean, DisposableBean {
    private static final String BOT_MESSAGE = "bot_message";
    private static final String TYPE = "type";
    private static final String SUBTYPE = "subtype";
    private static final String GOODBYE_MESSAGE = "goodbye";

    private final SlackDispatcher slackDispatcher;
    private final EventQueue eventQueue;
    private String token;
    private SlackRealTimeMessagingClient slackRealTimeMessagingClient;

    public SlackEventSensor(String token, SlackDispatcher slackDispatcher, EventQueue eventQueue) {
        this.slackDispatcher = slackDispatcher;
        this.eventQueue = eventQueue;
        this.token = token;
    }

    private void refreshSlackClient() {
        if(slackRealTimeMessagingClient != null) {
            this.slackRealTimeMessagingClient.close();
        }
        slackRealTimeMessagingClient = SlackClientFactory.createSlackRealTimeMessagingClient(token, null, null);
        slackRealTimeMessagingClient.addListener(MESSAGE, this);
        slackRealTimeMessagingClient.connect();
    }

    @Override
    public void onMessage(JsonNode message) {
        if(isBot(message)) {
            return;
        }
        if(isGoodbye(message)) {
            log.info("Slack Client Refresh.");
            refreshSlackClient();
        }
        this.eventQueue.offer(new Event<>(slackDispatcher, message));
    }

    private boolean isGoodbye(JsonNode message) {
        JsonNode jsonNode = message.get(TYPE);
        if(jsonNode == null) {
            return false;
        }
        return GOODBYE_MESSAGE.equals(jsonNode.asText());
    }

    private boolean isBot(JsonNode message) {
        JsonNode jsonNode = message.get(SUBTYPE);
        if(jsonNode == null) {
            return false;
        }
        return BOT_MESSAGE.equals(jsonNode.asText());
    }

    @Override
    public void destroy() throws Exception {
        this.slackRealTimeMessagingClient.close();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.refreshSlackClient();
        log.info("[BOT] Registered SlackDispatcher.");
    }
}
