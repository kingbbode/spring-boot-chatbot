package com.github.kingbbode.chatbot.core.brain;

import com.github.kingbbode.chatbot.core.brain.cell.AbstractBrainCell;
import com.github.kingbbode.chatbot.core.brain.factory.BrainFactory;
import com.github.kingbbode.chatbot.core.common.request.BrainRequest;
import com.github.kingbbode.chatbot.core.common.result.BrainResult;
import com.github.kingbbode.chatbot.core.conversation.Conversation;
import com.github.kingbbode.chatbot.core.conversation.ConversationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by YG on 2017-01-23.
 */
public class DispatcherBrain {
    private static final Logger logger = LoggerFactory.getLogger(BrainFactory.class);

    @Autowired
    private BrainFactory brainFactory;
    
    @Autowired
    private ConversationService conversationService;

    public BrainResult execute(BrainRequest brainRequest) {
        try {
            return selectedBrainCell(brainRequest).execute(brainRequest);
        } catch (Exception e) {
            logger.warn("execute error -{}", e);
        }
        return null;
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
                public BrainResult execute(BrainRequest brainRequest) throws InvocationTargetException, IllegalAccessException {
                    return BrainResult.builder()
                            .message("취소되었습니다.")
                            .room(brainRequest.getRoom())
                            .type(BrainResult.DEFAULT_RESULT_TYPE)
                            .build();
                }
            };
        }
        return (T) new AbstractBrainCell(){
            @Override
            public BrainResult execute(BrainRequest brainRequest) throws InvocationTargetException, IllegalAccessException {
                return BrainResult.builder()
                        .message(info.example())
                        .room(brainRequest.getRoom())
                        .type(BrainResult.DEFAULT_RESULT_TYPE)
                        .build();
            }
        };
    }
}
