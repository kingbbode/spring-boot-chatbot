package com.github.kingbbode.chatbot.core.brain;

import com.github.kingbbode.chatbot.core.brain.cell.AbstractBrainCell;
import com.github.kingbbode.chatbot.core.brain.factory.BrainFactory;
import com.github.kingbbode.chatbot.core.common.request.BrainRequest;
import com.github.kingbbode.chatbot.core.common.result.BrainResult;
import com.github.kingbbode.chatbot.core.common.result.DefaultBrainResult;
import com.github.kingbbode.chatbot.core.conversation.Conversation;
import com.github.kingbbode.chatbot.core.conversation.ConversationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by YG on 2017-01-23.
 */
@Slf4j
@RequiredArgsConstructor
public class DefaultDispatcherBrain implements DispatcherBrain {
    private final BrainFactory brainFactory;
    private final ConversationService conversationService;
    private final DistributedEnvironment distributedEnvironment;

    @Override
    public BrainResult execute(BrainRequest brainRequest) {
        try {
            if(distributedEnvironment.sync(brainRequest)) {
                return selectedBrainCell(brainRequest).execute(brainRequest);
            }
        } catch (Exception e) {
            log.warn("execute error -{}", e.getMessage(), e);
        }
        return DefaultBrainResult.NONE;
    }

    private <T extends AbstractBrainCell> T selectedBrainCell(BrainRequest brainRequest) throws IOException {
        Conversation conversation = conversationService.getLatest(brainRequest.getUser());
        if(conversation != null && brainFactory.containsConversationInfo(conversation.getFunction())){
            return conversation(brainRequest, conversation);
        }
        return brainFactory.get(brainRequest.getContent());
    }

    @SuppressWarnings("unchecked")
    private <T extends AbstractBrainCell> T conversation(BrainRequest brainRequest, Conversation conversation) {
        BrainFactory.ConversationInfo info = brainFactory.getConversationInfo(conversation.getFunction());
        String functionKey = info.findFunctionKey(brainRequest.getContent());
        if(functionKey != null && brainFactory.containsFunctionKey(functionKey)){
            brainRequest.setConversation(conversation);
            return brainFactory.getByFunctionKey(functionKey);
        }
        if(brainRequest.getContent().equals("취소")){
            conversationService.delete(brainRequest.getUser());
            return (T) new AbstractBrainCell(){
                @Override
                public DefaultBrainResult execute(BrainRequest brainRequest) throws InvocationTargetException, IllegalAccessException {
                    return DefaultBrainResult.builder()
                            .message("취소되었습니다.")
                            .room(brainRequest.getRoom())
                            .thread(brainRequest.getThread())
                            .build();
                }
            };
        }
        return (T) new AbstractBrainCell(){
            @Override
            public DefaultBrainResult execute(BrainRequest brainRequest) throws InvocationTargetException, IllegalAccessException {
                return DefaultBrainResult.builder()
                        .message(info.example())
                        .room(brainRequest.getRoom())
                        .thread(brainRequest.getThread())
                        .build();
            }
        };
    }
}
