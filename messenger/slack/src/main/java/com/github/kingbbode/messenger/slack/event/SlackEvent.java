package com.github.kingbbode.messenger.slack.event;

import lombok.Builder;
import lombok.Getter;

@Getter
public class SlackEvent {
    private final String clientMsgId;
    private final String channel;
    private final String thead;
    private final String user;
    private final String value;

    @Builder
    public SlackEvent(String clientMsgId, String channel, String thead, String user, String value) {
        this.clientMsgId = clientMsgId;
        this.channel = channel;
        this.thead = thead;
        this.user = user;
        this.value = value;
    }
}
