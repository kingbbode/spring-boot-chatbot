package com.github.kingbbode.messenger.slack;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.kingbbode.chatbot.core.brain.DispatcherBrain;
import com.github.kingbbode.chatbot.core.common.request.BrainRequest;
import com.github.kingbbode.chatbot.core.common.result.BrainResult;

public class SlackDispatcher {

    private static final String CHANNEL = "channel";
    private static final String TEXT = "text";
    private static final String USER = "user";

    private final DispatcherBrain dispatcherBrain;

    public SlackDispatcher(DispatcherBrain dispatcherBrain) {
        this.dispatcherBrain = dispatcherBrain;
    }

    public BrainResult dispatch(JsonNode message) {
        String channel = message.get(CHANNEL).asText();
        String text = message.get(TEXT).asText();
        String user = message.get(USER).asText();

        return dispatcherBrain.execute(BrainRequest.builder()
                .user(user)
                .room(channel)
                .content(text)
                .build());
    }
}
