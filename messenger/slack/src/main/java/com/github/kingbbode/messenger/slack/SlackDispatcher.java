package com.github.kingbbode.messenger.slack;

import com.github.kingbbode.chatbot.core.common.interfaces.Dispatcher;
import com.github.kingbbode.chatbot.core.common.request.BrainRequest;
import com.github.kingbbode.chatbot.core.common.result.BrainResult;
import com.slack.api.Slack;
import com.slack.api.model.event.MessageEvent;
import com.slack.api.rtm.RTMClient;
import com.slack.api.rtm.message.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import java.io.IOException;

@Slf4j
public class SlackDispatcher implements Dispatcher<MessageEvent>, InitializingBean, DisposableBean {

    private static final String CHANNEL = "channel";
    private static final String TEXT = "text";
    private static final String USER = "user";
    private final RTMClient rtm;

    public SlackDispatcher(String token) throws IOException {
        this.rtm = Slack.getInstance().rtm(token);
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
        rtm.sendMessage(Message.builder()
            .channel(result.getRoom())
            .text(result.getMessage())
            .build()
            .toJSONString()
        );
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        rtm.connect();
        log.info("[BOT] Registered SlackDispatcher.");
    }

    @Override
    public void destroy() throws Exception {
        rtm.disconnect();
    }
}
