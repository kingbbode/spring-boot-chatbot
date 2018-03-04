package com.github.kingbbode.messenger.telegram;

import com.github.kingbbode.chatbot.core.brain.DispatcherBrain;
import com.github.kingbbode.chatbot.core.common.request.BrainRequest;
import org.telegram.telegrambots.api.objects.Update;

/**
 * Created by YG-MAC on 2018. 3. 4..
 */
public class TelegramDispatcher {

    private DispatcherBrain dispatcherBrain;

    public TelegramDispatcher(DispatcherBrain dispatcherBrain) {
        this.dispatcherBrain = dispatcherBrain;
    }

    String dispatch(Update update) {
        BrainRequest brainRequest = BrainRequest.builder()
                .user(String.valueOf(update.getMessage().getFrom().getId()))
                .room(update.getMessage().getMediaGroupId())
                .content(update.getMessage().getText())
                .messageNo(String.valueOf(update.getMessage().getChatId()))
                .build();
        return dispatcherBrain.execute(brainRequest).getMessage();
    }
}
