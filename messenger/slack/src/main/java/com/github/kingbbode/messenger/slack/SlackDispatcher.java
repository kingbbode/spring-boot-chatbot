package com.github.kingbbode.messenger.slack;

import allbegray.slack.SlackClientFactory;
import allbegray.slack.webapi.SlackWebApiClient;
import com.fasterxml.jackson.databind.JsonNode;
import com.github.kingbbode.chatbot.core.common.interfaces.Dispatcher;
import com.github.kingbbode.chatbot.core.common.request.BrainRequest;
import com.github.kingbbode.chatbot.core.common.result.BrainResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;

@Slf4j
public class SlackDispatcher implements Dispatcher<JsonNode>, InitializingBean {

    private static final String CHANNEL = "channel";
    private static final String TEXT = "text";
    private static final String USER = "user";

    private final SlackWebApiClient webApiClient;

    public SlackDispatcher(String token) {
        this.webApiClient = SlackClientFactory.createWebApiClient(token);
    }

    public BrainRequest dispatch(JsonNode message) {
        String channel = message.get(CHANNEL).asText();
        String text = message.get(TEXT).asText();
        String user = message.get(USER).asText();

        return BrainRequest.builder()
                .user(user)
                .room(channel)
                .content(text)
                .build();
    }

    @Override
    public void onMessage(BrainResult result) {
        this.webApiClient.postMessage(result.getRoom(), result.getMessage());
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("[BOT] Registered SlackDispatcher.");
    }
}
