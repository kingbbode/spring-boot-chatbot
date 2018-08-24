package com.github.kingbbode.messenger.slack;

import allbegray.slack.SlackClientFactory;
import allbegray.slack.rtm.EventListener;
import allbegray.slack.rtm.SlackRealTimeMessagingClient;
import com.fasterxml.jackson.databind.JsonNode;
import com.github.kingbbode.chatbot.core.event.Event;
import com.github.kingbbode.chatbot.core.event.EventQueue;
import org.springframework.beans.factory.DisposableBean;

import javax.annotation.PostConstruct;

import static allbegray.slack.rtm.Event.MESSAGE;

public class SlackEventSensor implements EventListener, DisposableBean {
    private final SlackRealTimeMessagingClient slackRealTimeMessagingClient;
    private final SlackDispatcher slackDispatcher;
    private final EventQueue eventQueue;

    public SlackEventSensor(String token, SlackDispatcher slackDispatcher, EventQueue eventQueue) {
        this.slackRealTimeMessagingClient = SlackClientFactory.createSlackRealTimeMessagingClient(token);
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
        this.eventQueue.offer(new Event<>(slackDispatcher, message));
    }

    @Override
    public void destroy() throws Exception {
        this.slackRealTimeMessagingClient.close();
    }
}
