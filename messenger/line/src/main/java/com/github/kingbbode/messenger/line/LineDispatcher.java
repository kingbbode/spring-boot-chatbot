package com.github.kingbbode.messenger.line;

import com.github.kingbbode.chatbot.core.brain.DispatcherBrain;
import com.github.kingbbode.chatbot.core.common.interfaces.Dispatcher;
import com.github.kingbbode.chatbot.core.common.request.BrainRequest;
import com.github.kingbbode.chatbot.core.common.result.BrainResult;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import org.springframework.beans.factory.annotation.Autowired;

public class LineDispatcher implements Dispatcher<MessageEvent<TextMessageContent>, String> {

    @Autowired
    private DispatcherBrain dispatcherBrain;

    @Override
    public String dispatch(MessageEvent<TextMessageContent> event) {
        BrainRequest brainRequest = BrainRequest.builder()
                .user(event.getSource().getUserId())
                .room(event.getReplyToken().trim())
                .content(event.getMessage().getText())
                .messageNo(event.getMessage().getId())
                .build();
        BrainResult brainResult = dispatcherBrain.execute(brainRequest);
        return brainResult.getMessage();
    }
}
