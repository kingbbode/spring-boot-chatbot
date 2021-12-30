package com.github.kingbbode.messenger.slack.event;

import lombok.Builder;
import lombok.Getter;

@Getter
public class SlackEvent {
    private final String clientMsgId;
    private final String channel;
    private final String user;
    private final String value;

    @Builder
    public SlackEvent(String clientMsgId, String channel, String user, String value) {
        this.clientMsgId = clientMsgId;
        this.channel = channel;
        this.user = user;
        this.value = value;
    }
}
