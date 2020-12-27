package com.github.kingbbode.messenger.slack;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.kingbbode.chatbot.core.event.Event;
import com.github.kingbbode.chatbot.core.event.EventQueue;
import com.slack.api.Slack;
import com.slack.api.model.event.MessageEvent;
import com.slack.api.rtm.RTMClient;
import com.slack.api.rtm.RTMEventHandler;
import com.slack.api.rtm.RTMEventsDispatcher;
import com.slack.api.rtm.RTMEventsDispatcherFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.Objects;

@Slf4j
public class SlackEventSensor extends RTMEventHandler<MessageEvent> implements InitializingBean, DisposableBean {
    private static final String BOT_MESSAGE = "bot_message";
    private static final String SUBTYPE = "subtype";
    private static final String GOODBYE_MESSAGE = "goodbye";

    private final SlackDispatcher slackDispatcher;
    private final EventQueue eventQueue;
    private final RTMClient rtm;

    public SlackEventSensor(String token, SlackDispatcher slackDispatcher, EventQueue eventQueue) throws IOException {
        this.slackDispatcher = slackDispatcher;
        this.eventQueue = eventQueue;
        this.rtm = Slack.getInstance().rtm(token);
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
        rtm.close();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        RTMEventsDispatcher dispatcher = RTMEventsDispatcherFactory.getInstance();
        dispatcher.register(this);
        rtm.addMessageHandler(dispatcher.toMessageHandler());
        rtm.connect();
        log.info("[BOT] Registered EventSensor.");
    }

    @Override
    public void handle(MessageEvent event) {
        if(!StringUtils.isEmpty(event.getBotId()) || !Objects.isNull(event.getEdited())) {
            return;
        }
        this.eventQueue.offer(new Event<>(slackDispatcher, event));
    }
}
