package com.github.kingbbode.messenger.teamup.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Created by YG on 2017-04-05.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class FeedGroupsResponse {
    
    private List<FeedGroup> feedgroups;

    public List<FeedGroup> getFeedgroups() {
        return feedgroups;
    }

    public void setFeedgroups(List<FeedGroup> feedgroups) {
        this.feedgroups = feedgroups;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class FeedGroup {
        private Integer team;
        private Long feedgroup;
        private String groupname;
        private Integer watchfeed;
        private Integer writable;
        private Integer alerttype;
        private Integer alertfeed;
        private Integer alertreply;
        private Integer star;

        public Integer getTeam() {
            return team;
        }
    
        public void setTeam(Integer team) {
            this.team = team;
        }
    
        public Long getFeedgroup() {
            return feedgroup;
        }
    
        public void setFeedgroup(Long feedgroup) {
            this.feedgroup = feedgroup;
        }
    
        public String getGroupname() {
            return groupname;
        }
    
        public void setGroupname(String groupname) {
            this.groupname = groupname;
        }
    
        public Integer getWatchfeed() {
            return watchfeed;
        }
    
        public void setWatchfeed(Integer watchfeed) {
            this.watchfeed = watchfeed;
        }
    
        public Integer getWritable() {
            return writable;
        }
    
        public void setWritable(Integer writable) {
            this.writable = writable;
        }
    
        public Integer getAlerttype() {
            return alerttype;
        }
    
        public void setAlerttype(Integer alerttype) {
            this.alerttype = alerttype;
        }
    
        public Integer getAlertfeed() {
            return alertfeed;
        }
    
        public void setAlertfeed(Integer alertfeed) {
            this.alertfeed = alertfeed;
        }
    
        public Integer getAlertreply() {
            return alertreply;
        }
    
        public void setAlertreply(Integer alertreply) {
            this.alertreply = alertreply;
        }
    
        public Integer getStar() {
            return star;
        }
    
        public void setStar(Integer star) {
            this.star = star;
        }
    }
}
