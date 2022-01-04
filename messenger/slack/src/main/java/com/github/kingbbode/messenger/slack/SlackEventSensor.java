package com.github.kingbbode.messenger.slack;

import com.github.kingbbode.chatbot.core.brain.DispatcherBrain;
import com.github.kingbbode.chatbot.core.event.Event;
import com.github.kingbbode.chatbot.core.event.EventQueue;
import com.github.kingbbode.messenger.slack.event.BlockActionDispatcherBrain;
import com.github.kingbbode.messenger.slack.event.SlackEvent;
import com.slack.api.app_backend.events.payload.EventsApiPayload;
import com.slack.api.bolt.App;
import com.slack.api.bolt.context.builtin.ActionContext;
import com.slack.api.bolt.context.builtin.EventContext;
import com.slack.api.bolt.handler.BoltEventHandler;
import com.slack.api.bolt.request.builtin.BlockActionRequest;
import com.slack.api.bolt.response.Response;
import com.slack.api.methods.SlackApiException;
import com.slack.api.model.event.MessageEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Slf4j
public class SlackEventSensor implements InitializingBean, BoltEventHandler<MessageEvent> {

    private final App app;
    private final List<BlockActionDispatcherBrain> blockActionConnectors;
    private final SlackDispatcher slackDispatcher;
    private final EventQueue eventQueue;
    private final DispatcherBrain dispatcherBrain;

    public SlackEventSensor(
        App app,
        List<BlockActionDispatcherBrain> blockActionConnectors,
        SlackDispatcher slackDispatcher,
        EventQueue eventQueue,
        DispatcherBrain dispatcherBrain) {
        this.app = app;
        this.blockActionConnectors = blockActionConnectors;
        this.slackDispatcher = slackDispatcher;
        this.eventQueue = eventQueue;
        this.dispatcherBrain = dispatcherBrain;
    }

    @Override
    public Response apply(EventsApiPayload<MessageEvent> eventsApiPayload, EventContext context) throws IOException, SlackApiException {
        MessageEvent event = eventsApiPayload.getEvent();
        if(!StringUtils.isEmpty(event.getBotId()) || !Objects.isNull(event.getEdited())) {
            return context.ack();
        }

        SlackEvent slackEvent = SlackEvent.builder()
            .clientMsgId(event.getClientMsgId())
            .user(event.getUser())
            .channel(event.getChannel())
            .thead(StringUtils.hasText(event.getThreadTs()) ? event.getThreadTs() : event.getTs())
            .value(event.getText())
            .build();

        this.eventQueue.offer(new Event<>(slackDispatcher, slackEvent, dispatcherBrain));
        return context.ack();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        app.event(MessageEvent.class, this);
        log.info("[BOT] Connect Slack BOT.");
        blockActionConnectors.forEach(connector ->
            app.blockAction(connector.getActionId(), (blockActionRequest, context) -> connect(connector, blockActionRequest, context))
        );
        log.info("[BOT] Connect Block Action. count={}", blockActionConnectors.size());
    }

    private Response connect(BlockActionDispatcherBrain connector, BlockActionRequest blockActionRequest, ActionContext context) {
        blockActionRequest.getPayload().getActions().stream().findFirst()
            .ifPresent(action -> {
                SlackEvent slackEvent = SlackEvent.builder()
                    .clientMsgId(blockActionRequest.getPayload().getTriggerId())
                    .user(blockActionRequest.getPayload().getUser().getId())
                    .channel(blockActionRequest.getPayload().getChannel().getId())
                    .thead(StringUtils.hasText(blockActionRequest.getPayload().getMessage().getThreadTs()) ? blockActionRequest.getPayload().getMessage().getThreadTs() : blockActionRequest.getPayload().getMessage().getTs())
                    .value(action.getValue())
                    .build();
                this.eventQueue.offer(new Event<>(slackDispatcher, slackEvent, connector.dispatcher()));
            });
        return context.ack();
    }
}
