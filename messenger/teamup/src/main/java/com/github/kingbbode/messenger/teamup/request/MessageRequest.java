package com.github.kingbbode.messenger.teamup.request;

/**
 * Created by YG on 2016-03-28.
 */
public class MessageRequest {
    public MessageRequest(String content) {
        this.content = content;
    }

    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
