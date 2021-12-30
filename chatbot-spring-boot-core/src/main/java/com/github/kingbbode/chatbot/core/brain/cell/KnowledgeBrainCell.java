package com.github.kingbbode.chatbot.core.brain.cell;

import com.github.kingbbode.chatbot.core.base.knowledge.component.KnowledgeComponent;
import com.github.kingbbode.chatbot.core.common.request.BrainRequest;
import com.github.kingbbode.chatbot.core.common.result.DefaultBrainResult;

import java.util.List;
import java.util.Random;

/**
 * Created by YG on 2017-01-26.
 */
public class KnowledgeBrainCell extends AbstractBrainCell {
    private KnowledgeComponent knowledgeComponent;

    public KnowledgeBrainCell(KnowledgeComponent knowledgeComponent) {
        this.knowledgeComponent = knowledgeComponent;
    }

    @Override
    public String explain() {
        return "사용자 학습 기능 입니다.";
    }
    
    @Override
    public DefaultBrainResult execute(BrainRequest brainRequest) {
        List<String> results = knowledgeComponent.get(brainRequest.getContent());
        if(results == null){
            return DefaultBrainResult.NONE;
        }

        return DefaultBrainResult.builder()
                .message(results.get(new Random().nextInt(results.size())))
                .room(brainRequest.getRoom())
                .build();
    }
}
