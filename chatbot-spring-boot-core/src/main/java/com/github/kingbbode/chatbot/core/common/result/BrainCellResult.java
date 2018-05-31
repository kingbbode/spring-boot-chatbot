package com.github.kingbbode.chatbot.core.common.result;

/**
 * Created by YG on 2017-01-26.
 */
public class BrainCellResult {
    private String message;
    private String room;
    private Boolean example;

    public BrainCellResult(Builder builder) {
        this.message = builder.message;
        this.room = builder.room;
        this.example = builder.example;
    }

    public String getMessage() {
        return message;
    }

    public String getRoom() {
        return room;
    }
    
    public void comment(String comment){
        this.message += comment;
    }

    public static class Builder {
        private String message;
        private String room;
        private Boolean example = false;

        public Builder message(String message){
            this.message = message;
            return this;
        }

        public Builder room(String room){
            this.room = room;
            return this;
        }

        public Builder example(boolean example){
            this.example = example;
            return this;
        }

        public BrainCellResult build() {
            return new BrainCellResult(this);
        }
    }
}
