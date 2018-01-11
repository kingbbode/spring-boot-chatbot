package com.github.kingbbode.messenger.teamup;

import com.github.kingbbode.chatbot.core.brain.DispatcherBrain;
import com.github.kingbbode.chatbot.core.common.interfaces.Dispatcher;
import com.github.kingbbode.chatbot.core.common.request.BrainRequest;
import com.github.kingbbode.chatbot.core.common.result.BrainResult;
import com.github.kingbbode.messenger.teamup.enums.ResponseType;
import com.github.kingbbode.messenger.teamup.message.MessageService;
import com.github.kingbbode.messenger.teamup.response.EventResponse;
import com.github.kingbbode.messenger.teamup.response.MessageResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by YG on 2017-07-10.
 */
public class TeamUpDispatcher implements Dispatcher<EventResponse.Event, Integer> {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    
    private static final String EVENT_MESSAGE = "chat.message";
    private static final String EVENT_JOIN = "chat.join";
    private static final int MESSAGE_TYPE = 1;

    @Autowired
    private MessageService messageService;
    
    @Autowired
    private DispatcherBrain dispatcherBrain;
    
    @Autowired
    private TeamUpProperties teamUpProperties;

    @Override
    public Integer dispatch(EventResponse.Event event) {
        if (EVENT_MESSAGE.equals(event.getType())) {
            if (!teamUpProperties.getBot().contains(event.getChat().getUser())) {
                classification(event);
            }
        } else if (EVENT_JOIN.equals(event.getType())) {
            send(BrainResult.Builder.GREETING.room( event.getChat().getRoom()).build());
        }
        return 0;
    }

    private void classification(EventResponse.Event event) {
        EventResponse.Event.Chat chat = event.getChat();
        MessageResponse.Message message = messageService.readMessage(event.getChat());
        if(MESSAGE_TYPE != message.getType()) {
            return;
        }
        BrainRequest brainRequest = BrainRequest.builder()
                                    .user(String.valueOf(message.getUser()))
                                    .room(chat.getRoom())
                                    .content(message.getContent()!=null?message.getContent().trim():null)
                                    .messageNo(String.valueOf(message.getMsg()))
                                    .team(chat.getTeam())
                                    .build();
        if (!brainRequest.isValid()) {
            return;
        }
        send(dispatcherBrain.execute(brainRequest));
    }

    private void send(BrainResult result) {
        switch (ResponseType.resolve(result.type())) {
            case MESSAGE:
                messageService.sendMessage(result);
                break;
            case FEED:
                messageService.writeFeed(result);
                break;
            case OUT:
                messageService.outRoom(result);
                break;
        }
    }
}
