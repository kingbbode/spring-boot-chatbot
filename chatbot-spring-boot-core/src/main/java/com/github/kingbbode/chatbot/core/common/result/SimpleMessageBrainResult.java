package com.github.kingbbode.chatbot.core.common.result;

public abstract class SimpleMessageBrainResult implements BrainResult {
    protected String message;

    public String getMessage() {
        return message;
    }

    public void comment(String comment) {
        message += comment;
    }
}
