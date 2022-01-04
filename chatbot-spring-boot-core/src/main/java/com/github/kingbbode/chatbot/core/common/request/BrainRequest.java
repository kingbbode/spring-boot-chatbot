package com.github.kingbbode.chatbot.core.common.request;


import com.github.kingbbode.chatbot.core.conversation.Conversation;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by YG on 2017-01-23.
 */
@Getter
@Setter
public class BrainRequest {
    private String messenger;
    private String messageNo;
    private String user;
    private String room;
    private String thread;
    private String content;
    private Conversation conversation;

    @Builder
    public BrainRequest(String messenger, String messageNo, String user, String room, String thread, String content) {
        this.messenger = messenger;
        this.messageNo = messageNo;
        this.user = user;
        this.room = room;
        this.thread = thread;
        this.content = content;
    }

    @Override
    public String toString() {
        return "messenger=" + messenger + "\n" +
            "messageNo=" + messageNo + "\n" +
            "user=" + user + "\n" +
            "room=" + room + "\n" +
            "thread=" + thread + "\n" +
            "content=" + content + "\n";
    }
}
