package com.github.kingbbode.messenger.telegram;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.telegram.telegrambots.TelegramBotsApi;

@Slf4j
@RequiredArgsConstructor
public class TelegramBotsApiWrapper extends TelegramBotsApi implements InitializingBean {

    private final TelegramEventSensor telegramEventSensor;

    @Override
    public void afterPropertiesSet() throws Exception {
        this.registerBot(telegramEventSensor);
        log.info("[BOT] register telegram sensor.");
    }
}
