package com.github.kingbbode.messenger.teamup;

/**
 * Created by YG on 2017-07-10.
 */
public enum Api {
    FEED_WRITE("https://edge.tmup.com/v3/feed/"),
    FEED_LIST("https://edge.tmup.com/v3/feedgroups"),
    MESSAGE_READ("https://edge.tmup.com/v3/messages/"),
    MESSAGE_SEND("https://edge.tmup.com/v3/message/"),
    ROOM("https://edge.tmup.com/v3/room/"),
    RTM("https://ev.tmup.com/v3/events"),
    TOKEN("https://auth.tmup.com/oauth2/token"),
    FILE_DOWNLOAD("https://file.tmup.com/v3/file/"),
    FILE_UPLOAD("https://file.tmup.com/v3/files/"),
    AUTH_ORGANIGRAMME("https://auth.tmup.com/v1/team/");
    
    private String url;

    Api(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}
