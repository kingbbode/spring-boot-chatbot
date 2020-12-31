package com.github.kingbbode.messenger.telegram;

import com.github.kingbbode.chatbot.core.common.interfaces.Dispatcher;
import com.github.kingbbode.chatbot.core.common.request.BrainRequest;
import com.github.kingbbode.chatbot.core.common.result.BrainResult;
import com.github.kingbbode.chatbot.core.event.Event;
import com.github.kingbbode.chatbot.core.event.EventQueue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

/**
 * Created by YG-MAC on 2018. 3. 4..
 */
@Slf4j
public class TelegramEventSensor extends TelegramLongPollingBot implements Dispatcher<Update>, InitializingBean {

    private static final String MESSENGER = "TELEGRAM";
    private final String butUserName;
    private final String botToken;
    private final EventQueue eventQueue;

    public TelegramEventSensor(EventQueue eventQueue, String butUserName, String botToken) {
        super();
        this.butUserName = butUserName;
        this.botToken = botToken;
        this.eventQueue = eventQueue;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if(!update.hasMessage()) {
            return;
        }
        this.eventQueue.offer(new Event<>(this, update));
    }

    @Override
    public String getBotUsername() {
        return butUserName;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("[BOT] Registered TelegramEventSensor.");
    }

    @Override
    public BrainRequest dispatch(Update update) {
        return BrainRequest.builder()
                .user(String.valueOf(update.getMessage().getFrom().getId()))
                .room(String.valueOf(update.getMessage().getChatId()))
                .content(update.getMessage().getText())
                .messageNo(String.valueOf(update.getMessage().getChatId()))
                .messenger(MESSENGER)
                .build();
    }

    @Override
    public void onMessage(BrainRequest brainRequest, BrainResult result) {
        try {
            sendApiMethod(new SendMessage(result.getRoom(), result.getMessage()));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
