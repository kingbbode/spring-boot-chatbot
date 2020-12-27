package com.github.kingbbode.messenger.slack;

import com.github.kingbbode.chatbot.core.event.Event;
import com.github.kingbbode.chatbot.core.event.EventQueue;
import com.slack.api.model.event.MessageEvent;
import com.slack.api.rtm.RTMEventHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.StringUtils;

import java.util.Objects;

@Slf4j
public class SlackEventSensor extends RTMEventHandler<MessageEvent> implements InitializingBean {

    private final SlackDispatcher slackDispatcher;
    private final EventQueue eventQueue;
    private final SlackRTMClient slackRTMClient;

    public SlackEventSensor(SlackRTMClient slackRTMClient, SlackDispatcher slackDispatcher, EventQueue eventQueue) {
        this.slackDispatcher = slackDispatcher;
        this.eventQueue = eventQueue;
        this.slackRTMClient = slackRTMClient;
    }

    @Override
    public void afterPropertiesSet() {
        slackRTMClient.addMessageHandler(this);
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
