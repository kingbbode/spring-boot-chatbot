package com.github.kingbbode.chatbot.core.brain.aop;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.kingbbode.chatbot.core.base.stat.StatComponent;
import com.github.kingbbode.chatbot.core.brain.factory.BrainFactory;
import com.github.kingbbode.chatbot.core.common.annotations.BrainCell;
import com.github.kingbbode.chatbot.core.common.exception.InvalidReturnTypeException;
import com.github.kingbbode.chatbot.core.common.request.BrainRequest;
import com.github.kingbbode.chatbot.core.common.result.BrainCellResult;
import com.github.kingbbode.chatbot.core.conversation.Conversation;
import com.github.kingbbode.chatbot.core.conversation.ConversationService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;

/**
 * Created by YG-MAC on 2017. 1. 26..
 */
@Aspect
public class BrainCellAspect {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    
    @Autowired
    private ConversationService conversationService;
    
    @Autowired
    private BrainFactory brainFactory;

    @Autowired
    private StatComponent statComponent;

    @Around("@annotation(com.github.kingbbode.chatbot.core.common.annotations.BrainCell)")
    public Object checkArgLength(ProceedingJoinPoint joinPoint) throws Throwable {
        BrainCell brainCell = ((MethodSignature) joinPoint.getSignature()).getMethod().getAnnotation(BrainCell.class);
        BrainRequest request = (BrainRequest) joinPoint.getArgs()[0];
        if(!ObjectUtils.isEmpty(request.getConversation()) && brainCell.cancelable() && request.getContent().equals("취소")){
            conversationService.delete(request.getUser());
            return "초기화되었습니다.";
        }
        if(request.getContent().length() == 0){
            return "입력해주세요.";
        }
        Object object =  joinPoint.proceed();

        if(!(object instanceof String) && !(object instanceof BrainCellResult) ){
            throw new InvalidReturnTypeException(new Throwable());
        }
        
        conversation(brainCell, request);

        if("".equals(brainCell.parent())){
            statComponent.plus(brainCell.key());
        }
        
        return conversationComment(object, brainCell);
    }

    private Object  conversationComment(Object object, BrainCell brainCell) {
        if(!brainFactory.containsConversationInfo(brainCell.function())) {
            return object;
        }
        if(object instanceof String){
            object += "\n\n상태를 취소하려면 '취소'를 입력해주세요.\n30초 동안 아무 작업이 없을 시 자동 초기화됩니다.";
        }else if(object instanceof BrainCellResult){
            ((BrainCellResult) object).comment("\n상태를 취소하려면 '취소'를 입력해주세요.\n30초 동안 아무 작업이 없을 시 자동 초기화됩니다.");
        }
        return object;
    }

    private void conversation(BrainCell brainCell, BrainRequest request) throws JsonProcessingException {
        if(!brainFactory.containsConversationInfo(brainCell.function())) {
            if(!"".equals(brainCell.parent())){
                conversationService.delete(request.getUser());
            }
            return;
        }
        
        Conversation conversation = new Conversation(brainCell.function());
        conversation.put(request.getConversation());
        conversation.put("content", request.getContent());
        conversationService.push(request.getUser(), conversation);
    }
}
