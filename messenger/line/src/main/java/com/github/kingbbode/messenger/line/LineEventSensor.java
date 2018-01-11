package com.github.kingbbode.messenger.line;

import com.linecorp.bot.model.event.JoinEvent;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;
import org.springframework.beans.factory.annotation.Autowired;

@LineMessageHandler
public class LineEventSensor {

    @Autowired
    private LineDispatcher lineDispatcher;

    @EventMapping
    public TextMessage handleTextMessageEvent(MessageEvent<TextMessageContent> event) {
        return new TextMessage(lineDispatcher.dispatch(event));
    }

    @EventMapping
    public void joinEvent(JoinEvent event) {

    }
}
