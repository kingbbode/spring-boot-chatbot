package com.github.kingbbode.messenger.slack;

import com.github.kingbbode.chatbot.core.common.interfaces.Dispatcher;
import com.github.kingbbode.chatbot.core.common.request.BrainRequest;
import com.github.kingbbode.chatbot.core.common.result.BrainResult;
import com.slack.api.model.event.MessageEvent;
import com.slack.api.rtm.message.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;

import java.io.IOException;

@Slf4j
public class SlackDispatcher implements Dispatcher<MessageEvent>, InitializingBean {
    private final SlackRTMClient slackRTMClient;

    public SlackDispatcher(SlackRTMClient slackRTMClient) {
        this.slackRTMClient = slackRTMClient;
    }

    @Override
    public BrainRequest dispatch(MessageEvent message) {
        return BrainRequest.builder()
                .user(message.getUser())
                .room(message.getChannel())
                .content(message.getText())
                .build();
    }

    @Override
    public void onMessage(BrainResult result) {
        slackRTMClient.sendMessage(Message.builder()
            .channel(result.getRoom())
            .text(result.getMessage())
            .build()
        );
    }

    @Override
    public void afterPropertiesSet() {
        log.info("[BOT] Registered SlackDispatcher.");
    }
}
