package com.github.kingbbode.chatbot.autoconfigure.messenger.telegram;

import com.github.kingbbode.chatbot.core.event.EventQueue;
import com.github.kingbbode.messenger.telegram.TelegramBotsApiWrapper;
import com.github.kingbbode.messenger.telegram.TelegramEventSensor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.exceptions.TelegramApiRequestException;

/**
 * Created by YG-MAC on 2018. 3. 4..
 */
@Configuration
@EnableConfigurationProperties(TelegramProperties.class)
@ConditionalOnClass(name = "com.github.kingbbode.messenger.telegram.TelegramEventSensor")
@ConditionalOnProperty(prefix = "telegram", value = "token")
public class TelegramAutoConfiguration {

    public TelegramAutoConfiguration() {
        ApiContextInitializer.init();
    }

    @Bean
    @ConditionalOnMissingBean
    public TelegramEventSensor telegramEventSensor(EventQueue eventQueue, TelegramProperties telegramProperties) {
        return new TelegramEventSensor(eventQueue, telegramProperties.getName(), telegramProperties.getToken());
    }

    @Bean
    @ConditionalOnMissingBean(TelegramBotsApi.class)
    public TelegramBotsApi telegramBotsApi() throws TelegramApiRequestException {
        return new TelegramBotsApiWrapper(telegramEventSensor(null, null));
    }
}
