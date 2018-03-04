package com.github.kingbbode.chatbot.autoconfigure.messenger.telegram;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by YG-MAC on 2018. 3. 4..
 */
@ConfigurationProperties(prefix = "telegram")
@Data
public class TelegramProperties {
    private String name = "임시봇";
    private String token;
}
