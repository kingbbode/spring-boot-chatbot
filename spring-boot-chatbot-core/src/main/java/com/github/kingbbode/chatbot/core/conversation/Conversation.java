package com.github.kingbbode.chatbot.core.conversation;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by YG on 2017-04-03.
 */
public class Conversation {
    private String function;
    private Map<String, String> param;

    public Conversation() {
    }

    public Conversation(String function) {
        this.function = function;
        this.param = new HashMap<>();
    }

    public String getFunction() {
        return function;
    }

    public Map<String, String> getParam() {
        return param;
    }
    
    public void put(String key, String value) {
        param.put(key, value);
    }

    public void setParam(Map<String, String> param) {
        this.param = param;
    }

    public void setFunction(String function) {
        this.function = function;
    }

    public void put(Conversation conversation) {
        if(conversation != null) {
            this.param.putAll(conversation.param);
        }
    }
}
