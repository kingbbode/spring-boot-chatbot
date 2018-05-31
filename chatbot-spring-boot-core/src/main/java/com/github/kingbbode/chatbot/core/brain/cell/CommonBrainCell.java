package com.github.kingbbode.chatbot.core.brain.cell;

import com.github.kingbbode.chatbot.core.common.annotations.BrainCell;
import com.github.kingbbode.chatbot.core.common.exception.ArgumentInvalidException;
import com.github.kingbbode.chatbot.core.common.exception.BrainException;
import com.github.kingbbode.chatbot.core.common.exception.InvalidReturnTypeException;
import com.github.kingbbode.chatbot.core.common.request.BrainRequest;
import com.github.kingbbode.chatbot.core.common.result.BrainCellResult;
import com.github.kingbbode.chatbot.core.common.result.BrainResult;
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
            return BrainResult.Builder.FAILED.room(brainRequest.getRoom()).build();
        }
        Object result;
        try {
            result = active.invoke(object, brainRequest);
        }catch(Throwable e){
            if(e.getCause() instanceof BrainException){
                return BrainResult.builder()
                        .message(e.getCause().getMessage())
                        .room(brainRequest.getRoom())
                        .type(BrainResult.DEFAULT_RESULT_TYPE)
                        .build();
            }else if(e.getCause() instanceof ArgumentInvalidException){
                return BrainResult.builder()
                        .message(active.getAnnotation(BrainCell.class).example())
                        .room(brainRequest.getRoom())
                        .type(BrainResult.DEFAULT_RESULT_TYPE)
                        .build();
            }else if(e.getCause() instanceof InvalidReturnTypeException){
                return BrainResult.builder()
                        .message("Method Return Type Exception!")
                        .room(brainRequest.getRoom())
                        .type(BrainResult.DEFAULT_RESULT_TYPE)
                        .build();
            }
            return BrainResult.builder()
                    .message("Server Error : " + e.getMessage())
                    .room(brainRequest.getRoom())
                    .type(BrainResult.DEFAULT_RESULT_TYPE)
                    .build();
        }
        if(result instanceof BrainCellResult){
            return BrainResult.builder()
                    .result((BrainCellResult) result)
                    .type(brain.type())
                    .build();
        }        
        
        return BrainResult.builder()
                .message((String) result)
                .room(brainRequest.getRoom())
                .type(brain.type())
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
