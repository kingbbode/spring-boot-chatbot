package com.github.kingbbode.chatbot.core;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import redis.clients.jedis.Protocol;

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

    private boolean useExternalRedis = false;
    private String hostName = "localhost";
    private int port = Protocol.DEFAULT_PORT;
    private int timeout = Protocol.DEFAULT_TIMEOUT;
    private String password;
    private boolean usePool = true;
    private boolean useSsl = false;
    private int dbIndex = 0;
    private String clientName;
    private boolean convertPipelineAndTxResults = true;

    private String commandPrefix = "#";
}

