package com.github.kingbbode.messenger.teamup.templates.template;

import com.github.kingbbode.chatbot.core.common.properties.BotProperties;
import com.github.kingbbode.messenger.teamup.Api;
import com.github.kingbbode.messenger.teamup.TeamUpProperties;
import com.github.kingbbode.messenger.teamup.TeamUpTokenManager;
import com.github.kingbbode.messenger.teamup.request.MessageRequest;
import com.github.kingbbode.messenger.teamup.request.RoomCreateRequest;
import com.github.kingbbode.messenger.teamup.response.FeedGroupsResponse;
import com.github.kingbbode.messenger.teamup.response.MessageResponse;
import com.github.kingbbode.messenger.teamup.response.RoomCreateResponse;
import com.github.kingbbode.messenger.teamup.templates.BaseTemplate;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.client.RestOperations;

import javax.annotation.PostConstruct;

/**
 * Created by YG on 2016-10-13.
 */
public class EdgeTemplate extends BaseTemplate {
    
    private final BotProperties botProperties;

    private final TeamUpProperties teamUpProperties;

    public EdgeTemplate(TeamUpTokenManager tokenManager, RestOperations restOperations, BotProperties botProperties, TeamUpProperties teamUpProperties) {
        super(tokenManager, restOperations);
        this.botProperties = botProperties;
        this.teamUpProperties = teamUpProperties;
    }

    @Async
    public MessageResponse readMessage(String message, String room) {
        ParameterizedTypeReference<MessageResponse> p = new ParameterizedTypeReference<MessageResponse>() {
        };
        return get(Api.MESSAGE_READ.getUrl() + room + "/1/0/" + message, p);
        
    }

    @Async
    public void sendMessage(String message, String room) {
        if(!room.equals("999999999999") && !StringUtils.isEmpty(message)) {
            ParameterizedTypeReference<MessageResponse> p = new ParameterizedTypeReference<MessageResponse>() {
            };
            post(Api.MESSAGE_SEND.getUrl() + (botProperties.isTestMode()?teamUpProperties.getTestRoom():room), new MessageRequest(message), p);
        }
    }

    @Async
    public RoomCreateResponse openRoom(String userId) {
        ParameterizedTypeReference<RoomCreateResponse> p = new ParameterizedTypeReference<RoomCreateResponse>() {
        };
        return post(Api.ROOM.getUrl() + "1", new RoomCreateRequest(new int[]{Integer.parseInt(botProperties.isTestMode()?teamUpProperties.getTestUser():userId)}), p);
    }

    @Async
    public void writeFeed(String message, String room) {
        ParameterizedTypeReference<MessageResponse> p = new ParameterizedTypeReference<MessageResponse>() {
        };
        post(Api.FEED_WRITE.getUrl() + (botProperties.isTestMode()?teamUpProperties.getTestFeed():room),new MessageRequest(message), p);
    }

    @Async
    public void sendImage(String fileId, String room) {
        if(!room.equals("999999999999") && !StringUtils.isEmpty(fileId)) {
            ParameterizedTypeReference<MessageResponse> p = new ParameterizedTypeReference<MessageResponse>() {
            };
            post(Api.MESSAGE_SEND.getUrl() + (botProperties.isTestMode()?teamUpProperties.getTestRoom():room) +"/2", new MessageRequest(fileId), p);
        }
    }
    
    public void outRoom(String room) {
        if(!room.equals("999999999999") && !StringUtils.isEmpty(room)) {
            ParameterizedTypeReference<MessageResponse> p = new ParameterizedTypeReference<MessageResponse>() {
            };
            delete(Api.ROOM.getUrl() + "/" + room + "/" + System.currentTimeMillis() + room, null, p);
        }
    }
    
    public FeedGroupsResponse readFeedGroupList(){
        ParameterizedTypeReference<FeedGroupsResponse> p = new ParameterizedTypeReference<FeedGroupsResponse>() {
        };
        return get(Api.FEED_LIST.getUrl(), p);
    }
}
