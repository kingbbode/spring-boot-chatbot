package com.github.kingbbode.chatbot.autoconfigure.messenger.slack;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by YG-MAC on 2018. 3. 4..
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "slack")
public class SlackProperties {
    private String appToken;
    private String botToken;
}
