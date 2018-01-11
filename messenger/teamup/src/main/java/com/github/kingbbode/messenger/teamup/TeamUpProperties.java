package com.github.kingbbode.messenger.teamup;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * Created by YG on 2017-07-10.
 */
@ConfigurationProperties(prefix = "chatbot.teamup")
@Getter
@Setter
public class TeamUpProperties {
    private String id;
    private String password;
    private String clientId;
    private String clientSecret;
    private String testRoom;
    private String testFeed;
    private String testUser;
    private List<String> bot;
}
