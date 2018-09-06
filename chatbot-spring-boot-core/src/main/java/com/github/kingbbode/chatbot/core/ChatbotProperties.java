package com.github.kingbbode.chatbot.core;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by YG on 2017-07-10.
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "chatbot")
public class ChatbotProperties {
    private String name = "default";
    private String basePackage = "";
    private boolean enableBase = true;
    private boolean enableKnowledge = false;
    private String commandPrefix = "#";
}

