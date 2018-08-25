package com.github.kingbbode.messenger.line;

import com.linecorp.bot.model.event.JoinEvent;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;

@Slf4j
@LineMessageHandler
public class LineEventSensor implements InitializingBean {

    private LineDispatcher lineDispatcher;

    public LineEventSensor(LineDispatcher lineDispatcher) {
        this.lineDispatcher = lineDispatcher;
    }

    @EventMapping
    public TextMessage handleTextMessageEvent(MessageEvent<TextMessageContent> event) {
        return new TextMessage(lineDispatcher.dispatch(event));
    }

    @EventMapping
    public void joinEvent(JoinEvent event) {

    }

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("[BOT] Registered LineEventSensor.");
    }
}
