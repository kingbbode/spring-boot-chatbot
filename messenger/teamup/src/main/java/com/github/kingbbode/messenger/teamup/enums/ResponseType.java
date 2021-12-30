package com.github.kingbbode.messenger.teamup.enums;

import com.github.kingbbode.chatbot.core.common.result.DefaultBrainResult;

import java.util.Arrays;

public enum ResponseType {
    FEED("feed"),
    OUT("out"),
    MESSAGE(DefaultBrainResult.DEFAULT_RESULT_TYPE),
    NONE("");

    private final String value;

    ResponseType(String value) {
        this.value = value;
    }

    public static ResponseType resolve(String message) {
        return Arrays.stream(ResponseType.values())
                .filter(responseType -> responseType.value.equals(message))
                .findAny().orElse(NONE);
    }

    @Override
    public String toString() {
        return value;
    }
}
