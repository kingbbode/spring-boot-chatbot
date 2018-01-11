package com.github.kingbbode.messenger.teamup.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Arrays;
import java.util.List;

/**
 * Created by YG on 2016-09-12.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class MessageResponse {
    List<Message> msgs;

    public List<Message> getMsgs() {
        return msgs;
    }

    public void setMsgs(List<Message> msgs) {
        this.msgs = msgs;
    }
    
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Message{
        
        int msg;
        
        int user;
        
        int type;
        
        int len;
        
        String content;
        
        long created;
        
        String tagfeeds;
        
        int[] users;
        
        File file;

        public int getMsg() {
            return msg;
        }

        public void setMsg(int msg) {
            this.msg = msg;
        }

        public int getUser() {
            return user;
        }

        public void setUser(int user) {
            this.user = user;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getLen() {
            return len;
        }

        public void setLen(int len) {
            this.len = len;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public long getCreated() {
            return created;
        }

        public void setCreated(long created) {
            this.created = created;
        }

        public String getTagfeeds() {
            return tagfeeds;
        }

        public void setTagfeeds(String tagfeeds) {
            this.tagfeeds = tagfeeds;
        }

        public int[] getUsers() {
            return users==null?null:Arrays.copyOf(users, users.length);
        }

        public void setUsers(int[] users) {
            this.users = users==null?null:Arrays.copyOf(users, users.length);
        }

        public File getFile() {
            return file;
        }

        public void setFile(File file) {
            this.file = file;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class File{
        String name;
        
        int size;
        
        String id;
        
        int owner;
        
        String type;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public int getOwner() {
            return owner;
        }

        public void setOwner(int owner) {
            this.owner = owner;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
}
