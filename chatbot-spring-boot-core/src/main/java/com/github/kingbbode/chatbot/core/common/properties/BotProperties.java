package com.github.kingbbode.chatbot.core.common.properties;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by YG on 2016-10-13.
 */
@Getter
@Setter
public class BotProperties {
    private String name;
    private String commandPrefix;
    private boolean testMode;

    public BotProperties(String name, String commandPrefix, boolean testMode) {
        this.name = name;
        this.commandPrefix = commandPrefix;
        this.testMode = testMode;
    }
    
    public boolean isTestMode() {
        return testMode;
    }
}
