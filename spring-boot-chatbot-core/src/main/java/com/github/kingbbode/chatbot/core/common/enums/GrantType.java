package com.github.kingbbode.chatbot.core.common.enums;

/**
 * Created by YG on 2016-10-14.
 */
public enum GrantType {
    PASSWORD("password"),
    REFRESH("refresh_token");

    String key;

    GrantType(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
