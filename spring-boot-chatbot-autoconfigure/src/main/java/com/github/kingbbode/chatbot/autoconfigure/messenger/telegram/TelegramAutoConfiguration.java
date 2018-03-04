package com.github.kingbbode.chatbot.autoconfigure.messenger.telegram;

import com.github.kingbbode.chatbot.core.brain.DispatcherBrain;
import com.github.kingbbode.messenger.telegram.TelegramDispatcher;
import com.github.kingbbode.messenger.telegram.TelegramEventSensor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by YG-MAC on 2018. 3. 4..
 */
@Configuration
@ConditionalOnClass(name = "com.github.kingbbode.messenger.line.TelegramDispatcher")
@ConditionalOnProperty(prefix = "telegram", value = "token")
public class TelegramAutoConfiguration {

    @Autowired
    private TelegramProperties telegramProperties;

    @Bean
    @ConditionalOnMissingBean
    public TelegramDispatcher telegramDispatcher(DispatcherBrain dispatcherBrain) {
        return new TelegramDispatcher(dispatcherBrain);
    }

    @Bean
    @ConditionalOnMissingBean
    public TelegramEventSensor telegramEventSensor(TelegramDispatcher telegramDispatcher) {
        return new TelegramEventSensor(telegramDispatcher, telegramProperties.getName(), telegramProperties.getToken());
    }
}
