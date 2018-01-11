package com.github.kingbbode.messenger.teamup.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;

/**
 * Created by YG on 2016-03-28.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class EventResponse {
    private ArrayList<Event> events;

    public ArrayList<Event> getEvents() {
        return events;
    }

    public void setEvents(ArrayList<Event> events) {
        this.events = events;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Event{
        private String type;
        private Chat chat;
        private Feed feed;
        private Inform inform;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public Chat getChat() {
            return chat;
        }

        public void setChat(Chat chat) {
            this.chat = chat;
        }

        public Feed getFeed() {
            return feed;
        }

        public void setFeed(Feed feed) {
            this.feed = feed;
        }

        public Inform getInform() {
            return inform;
        }

        public void setInform(Inform inform) {
            this.inform = inform;
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Chat{
            private String team;
            private String room;
            private String msg;
            private String user;

            public String getTeam() {
                return team;
            }

            public void setTeam(String team) {
                this.team = team;
            }

            public String getRoom() {
                return room;
            }

            public void setRoom(String room) {
                this.room = room;
            }

            public String getMsg() {
                return msg;
            }

            public void setMsg(String msg) {
                this.msg = msg;
            }

            public String getUser() {
                return user;
            }

            public void setUser(String user) {
                this.user = user;
            }
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Feed{
            private String feedgroup;

            public String getFeedgroup() {
                return feedgroup;
            }

            public void setFeedgroup(String feedgroup) {
                this.feedgroup = feedgroup;
            }
        }
        
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Inform{
            private String feedgroup;

            public String getFeedgroup() {
                return feedgroup;
            }

            public void setFeedgroup(String feedgroup) {
                this.feedgroup = feedgroup;
            }
        }
    }

}
