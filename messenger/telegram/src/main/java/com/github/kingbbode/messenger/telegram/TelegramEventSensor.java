package com.github.kingbbode.messenger.telegram;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

/**
 * Created by YG-MAC on 2018. 3. 4..
 */
public class TelegramEventSensor extends TelegramLongPollingBot {

    private TelegramDispatcher telegramDispatcher;
    private String butUserName;
    private String botToken;

    public TelegramEventSensor(TelegramDispatcher telegramDispatcher, String butUserName, String botToken) {
        this.telegramDispatcher = telegramDispatcher;
        this.butUserName = butUserName;
        this.botToken = botToken;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if(!update.hasMessage()) {
            return;
        }
        SendMessage sendMessage = new SendMessage(update.getMessage().getChatId(), telegramDispatcher.dispatch(update));
        try {
            sendApiMethod(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        return butUserName;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }
}
