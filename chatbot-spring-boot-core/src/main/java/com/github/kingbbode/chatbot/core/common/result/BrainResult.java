package com.github.kingbbode.chatbot.core.common.result;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by YG on 2017-01-26.
 */
public class BrainResult {
    public static String DEFAULT_RESULT_TYPE = "message";
    public static BrainResult NONE = builder().build();

    private String type;
    private String message;
    private String room;

    public BrainResult(Builder builder) {
        this.type = builder.type;
        this.message = builder.message;
        this.room = builder.room;
    }

    public String type() {
        return type;
    }

    public String getMessage() {
        return message;
    }

    public String getRoom() {
        return room;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String type;
        private String message;
        private String room;

        public Builder(){
            this.type="";
        }

        public Builder result(BrainCellResult result){
            this.message(result.getMessage());
            if(!StringUtils.isEmpty(result.getRoom())) {
                this.room(result.getRoom());
            }
            return this;
        }

        public Builder type(String type) {
            this.type = type;
            return this;
        }

        public Builder message(String message){
            this.message = message;
            return this;
        }

        public Builder room(String room){
            this.room = room;
            return this;
        }

        public BrainResult build() {
            if(this.message == null || this.room == null){
                return NONE;
            }
            return new BrainResult(this);
        }
        public static Builder FAILED = new Builder()
                .message("해당 기능은 장애 상태 입니다");
        public static Builder GREETING = new Builder()
                .message("안녕하세요.\n '기능'을 참고하세요");
    }

}
