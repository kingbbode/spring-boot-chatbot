package com.github.kingbbode.messenger.teamup.request;

import com.github.kingbbode.chatbot.core.common.request.BrainRequest;
import org.springframework.http.MediaType;

/**
 * Created by YG on 2017-05-17.
 */
public class FileRequest {
    private String messageNo;
    private String fileId;
    private String team;
    private String fileName;
    private MediaType type;

    private FileRequest(String messageNo, String fileId, String team, String fileName, MediaType type) {
        this.messageNo = messageNo;
        this.fileId = fileId;
        this.team = team;
        this.fileName = fileName;
        this.type = type;
    }

    public String getMessageNo() {
        return messageNo;
    }

    public String getFileId() {
        return fileId;
    }

    public String getTeam() {
        return team;
    }

    public String getFileName() {
        return fileName;
    }

    public MediaType getType() {
        return type;
    }
    
    public String getParam(){
        return team + "/" + fileId + "?msg=" + messageNo;
    }

    public static class Builder {
        private String messageNo;
        private String fileId;
        private String team;
        private String fileName;
        private MediaType type;

        public Builder request(BrainRequest request){
            this.messageNo = request.getMessageNo();
            this.team = request.getTeam();
            return this;
        }

        public Builder id(String fileId){
            this.fileId = fileId;
            return this;
        }

        public Builder type(MediaType type){
            this.type = type;
            return this;
        }
        
        public Builder name(String fileName){
            this.fileName = System.currentTimeMillis() + fileName;
            return this;
        }
        
        public FileRequest build(){
            return new FileRequest(messageNo, fileId, team, fileName, type);
        }
        
    }
}
