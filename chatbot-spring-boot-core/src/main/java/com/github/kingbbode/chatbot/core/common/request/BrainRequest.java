package com.github.kingbbode.chatbot.core.common.request;


import com.github.kingbbode.chatbot.core.conversation.Conversation;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.StringUtils;

/**
 * Created by YG on 2017-01-23.
 */
@Getter
@Setter
public class BrainRequest {
    private String messenger;
    private String messageNo;
    private String team;
    private String user;
    private String room;
    private String content;
    private Conversation conversation;

    @Builder
    public BrainRequest(String messenger, String messageNo, String team, String user, String room, String content) {
        this.messenger = messenger;
        this.messageNo = messageNo;
        this.team = team;
        this.user = user;
        this.room = room;
        this.content = content;
    }

    /*
    public BrainRequest(EventResponse.Event.Chat chat, MessageResponse.Message message) {
        this.user = String.valueOf(message.getUser());
        this.room = chat.getRoom();
        this.content = message.getContent()!=null?message.getContent().trim():null;
        this.messageNo = String.valueOf(message.getMsg());
        this.team = String.valueOf(chat.getTeam());
    }

    public BrainRequest(String user, String room, String content) {
        this.user = user;
        this.room = room;
        this.content = content;
    }*/

    public boolean isValid(){
        return !StringUtils.isEmpty(content);
    }

    @Override
    public String toString() {
        return "BrainRequest{\n" +
                "team=" + team + "\n" + 
                "message no=" + messageNo + "\n" + 
                "user='" + user + "\n" +
                "room='" + room + "\n" +
                "\n}";
    }
}
