package com.github.kingbbode.messenger.slack;

import allbegray.slack.SlackClientFactory;
import allbegray.slack.rtm.Event;
import allbegray.slack.rtm.EventListener;
import allbegray.slack.rtm.SlackRealTimeMessagingClient;
import allbegray.slack.webapi.SlackWebApiClient;
import com.fasterxml.jackson.databind.JsonNode;
import com.github.kingbbode.chatbot.core.common.result.BrainResult;
import org.springframework.beans.factory.DisposableBean;

import javax.annotation.PostConstruct;

public class SlackEventSensor implements EventListener, DisposableBean {
    private final SlackRealTimeMessagingClient slackRealTimeMessagingClient;
    private final SlackWebApiClient webApiClient;
    private final SlackDispatcher slackDispatcher;

    public SlackEventSensor(String token, SlackDispatcher slackDispatcher) {
        this.slackRealTimeMessagingClient = SlackClientFactory.createSlackRealTimeMessagingClient(token);
        this.webApiClient = SlackClientFactory.createWebApiClient(token);
        this.slackDispatcher = slackDispatcher;
    }

    @PostConstruct
    public void init() {
        this.slackRealTimeMessagingClient.addListener(Event.MESSAGE, this);
        this.slackRealTimeMessagingClient.connect();
    }

    @Override
    public void onMessage(JsonNode message) {
        BrainResult result = slackDispatcher.dispatch(message);
        webApiClient.postMessage(result.getRoom(), result.getMessage());
    }

    @Override
    public void destroy() throws Exception {
        this.slackRealTimeMessagingClient.close();
    }
}
