package com.github.kingbbode.chatbot.core.base.knowledge.brain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.kingbbode.chatbot.core.base.knowledge.component.KnowledgeComponent;
import com.github.kingbbode.chatbot.core.common.annotations.Brain;
import com.github.kingbbode.chatbot.core.common.annotations.BrainCell;
import com.github.kingbbode.chatbot.core.common.exception.ArgumentInvalidException;
import com.github.kingbbode.chatbot.core.common.request.BrainRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

/**
 * Created by YG-MAC on 2017. 1. 26..
 */
@Brain
public class KnowledgeBrain {

    @Autowired
    private KnowledgeComponent knowledgeComponent;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @BrainCell(key = "학습", explain = "명령어 학습시키기", function = "addKnowledge")
    public String addKnowledge(BrainRequest brainRequest) throws JsonProcessingException {
        return "학습시킬 명령어가 무엇인가요?";
    }

        @BrainCell(parent = "addKnowledge", function = "addKnowledge2")
        public String addKnowledge2(BrainRequest brainRequest) {
            brainRequest.getConversation().put("key", brainRequest.getContent());
            return "이 명렁어에 기억할 내용은 무엇인가요?";
        }
    
            @BrainCell(parent = "addKnowledge2", function = "addKnowledge3", example = "입력하신 내용에 오류가 있습니다. 다시 입력해주세요.")
            public String addKnowledge3(BrainRequest brainRequest) {
                logger.info("addKnowledge user :{}, key : {}, content : {}", brainRequest.getUser(), brainRequest.getConversation().getParam().get("key"), brainRequest.getContent());
                try {
                    return knowledgeComponent.addKnowledge(brainRequest.getConversation().getParam().get("key"), brainRequest.getContent());
                } catch (JsonProcessingException e) {
                    throw new ArgumentInvalidException(e);
                }
            }

    @BrainCell(key = "까묵", explain = "학습 시킨 명령어 지우기", example = "까묵 명령어", function = "forgetKnowledge")
    public String forgetKnowledge(BrainRequest brainRequest) {
        return "무엇을 까먹을까요?";
    }

        @BrainCell(parent = "forgetKnowledge", function = "forgetKnowledge2")
        public String forgetKnowledge2(BrainRequest brainRequest) {
            return knowledgeComponent.forgetKnowledge(brainRequest.getContent());
        }
}