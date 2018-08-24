package com.github.kingbbode.messenger.teamup.templates.template;

import com.github.kingbbode.messenger.teamup.Api;
import com.github.kingbbode.messenger.teamup.TeamUpTokenManager;
import com.github.kingbbode.messenger.teamup.response.EventResponse;
import com.github.kingbbode.messenger.teamup.templates.BaseTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.RestOperations;

import javax.annotation.PostConstruct;

/**
 * Created by YG on 2016-10-13.
 */
public class EventTemplate extends BaseTemplate {


    public EventTemplate(TeamUpTokenManager tokenManager, RestOperations restOperations) {
        super(tokenManager, restOperations);
    }

    public EventResponse getEvent() {
        ParameterizedTypeReference<EventResponse> p = new ParameterizedTypeReference<EventResponse>() {
        };
        return get(Api.RTM.getUrl(),  p);
    }
}
