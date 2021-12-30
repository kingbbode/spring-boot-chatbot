package com.github.kingbbode.chatbot.core.brain.cell;

import com.github.kingbbode.chatbot.core.common.annotations.BrainCell;
import com.github.kingbbode.chatbot.core.common.exception.ArgumentInvalidException;
import com.github.kingbbode.chatbot.core.common.exception.BrainException;
import com.github.kingbbode.chatbot.core.common.exception.InvalidReturnTypeException;
import com.github.kingbbode.chatbot.core.common.request.BrainRequest;
import com.github.kingbbode.chatbot.core.common.result.BrainResult;
import com.github.kingbbode.chatbot.core.common.result.DefaultBrainResult;
import org.apache.commons.lang3.text.WordUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;

import java.lang.reflect.Method;

/**
 * Created by YG on 2017-01-23.
 */
public class CommonBrainCell extends AbstractBrainCell {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    
    private String name;
    private Method active;
    private Object object;
    private BeanFactory beanFactory;
    private BrainCell brain;

    public CommonBrainCell(BrainCell brain, Class<?> clazz, Method active, BeanFactory beanFactory) {
        this.name = WordUtils.uncapitalize(clazz.getSimpleName());
        this.brain = brain;
        this.active = active;
        this.beanFactory = beanFactory;
    }

    @Override
    public String explain() {
        return brain.explain();
    }

    @Override
    public BrainResult execute(BrainRequest brainRequest) {
        if (!inject()) {
            return DefaultBrainResult.Builder.FAILED.room(brainRequest.getRoom()).build();
        }
        Object result;
        try {
            result = active.invoke(object, brainRequest);
        }catch(Throwable e){
            if(e.getCause() instanceof BrainException){
                return DefaultBrainResult.builder()
                        .message(e.getCause().getMessage())
                        .room(brainRequest.getRoom())
                        .build();
            }else if(e.getCause() instanceof ArgumentInvalidException){
                return DefaultBrainResult.builder()
                        .message(active.getAnnotation(BrainCell.class).example())
                        .room(brainRequest.getRoom())
                        .build();
            }else if(e.getCause() instanceof InvalidReturnTypeException){
                return DefaultBrainResult.builder()
                        .message("Method Return Type Exception!")
                        .room(brainRequest.getRoom())
                        .build();
            }
            return DefaultBrainResult.builder()
                    .message("Server Error : " + e.getMessage())
                    .room(brainRequest.getRoom())
                    .build();
        }
        if(result instanceof String) {
            return DefaultBrainResult.builder()
                    .message((String) result)
                    .room(brainRequest.getRoom())
                    .build();
        } else if(result instanceof BrainResult) {
            return (BrainResult) result;
        }

        return DefaultBrainResult.builder()
            .room(brainRequest.getRoom())
            .message("Not Support Result Type.")
            .build();
    }

    private boolean inject() {
        if (object != null) {
            return true;
        }
        if (beanFactory.containsBean(name)) {
            object = beanFactory.getBean(name);
        }

        return object != null;
    }
}
