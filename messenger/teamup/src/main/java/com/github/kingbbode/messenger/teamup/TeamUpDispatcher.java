package com.github.kingbbode.messenger.teamup;

import com.github.kingbbode.chatbot.core.common.interfaces.Dispatcher;
import com.github.kingbbode.chatbot.core.common.request.BrainRequest;
import com.github.kingbbode.chatbot.core.common.result.DefaultBrainResult;
import com.github.kingbbode.messenger.teamup.enums.ResponseType;
import com.github.kingbbode.messenger.teamup.message.MessageService;
import com.github.kingbbode.messenger.teamup.response.EventResponse;
import com.github.kingbbode.messenger.teamup.response.MessageResponse;
import lombok.RequiredArgsConstructor;

/**
 * Created by YG on 2017-07-10.
 */
@RequiredArgsConstructor
public class TeamUpDispatcher implements Dispatcher<EventResponse.Event> {

    private static final String MESSENGER = "TEAMUP";
    private static final String EVENT_MESSAGE = "chat.message";
    private static final int MESSAGE_TYPE = 1;

    private final MessageService messageService;

    private final TeamUpProperties teamUpProperties;

    @Override
    public BrainRequest dispatch(EventResponse.Event event) {
        if (EVENT_MESSAGE.equals(event.getType())) {
            if (!teamUpProperties.getBot().contains(event.getChat().getUser())) {
                return classification(event);
            }
        } /*else if (EVENT_JOIN.equals(event.getType())) {
            return BrainResult.Builder.GREETING.room( event.getChat().getRoom()).build();
        }*/
        return skip();
    }

    @Override
    public void onMessage(BrainRequest request, DefaultBrainResult result) {
        send(result);
    }

    private BrainRequest classification(EventResponse.Event event) {
        EventResponse.Event.Chat chat = event.getChat();
        MessageResponse.Message message = messageService.readMessage(event.getChat());
        if(MESSAGE_TYPE != message.getType()) {
            return skip();
        }
        BrainRequest brainRequest = BrainRequest.builder()
                                    .messenger(MESSENGER)
                                    .user(String.valueOf(message.getUser()))
                                    .room(chat.getRoom())
                                    .content(message.getContent()!=null?message.getContent().trim():null)
                                    .messageNo(String.valueOf(message.getMsg()))
                                    .team(chat.getTeam())
                                    .build();
        if (!brainRequest.isValid()) {
            return skip();
        }
        return brainRequest;
    }

    private void send(DefaultBrainResult result) {
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
