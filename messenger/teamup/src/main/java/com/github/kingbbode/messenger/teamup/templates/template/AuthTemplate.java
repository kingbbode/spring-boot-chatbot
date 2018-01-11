package com.github.kingbbode.messenger.teamup.templates.template;

import com.github.kingbbode.messenger.teamup.Api;
import com.github.kingbbode.messenger.teamup.response.OrganigrammeResponse;
import com.github.kingbbode.messenger.teamup.response.RoomInfoResponse;
import com.github.kingbbode.messenger.teamup.templates.BaseTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.RestOperations;

import javax.annotation.PostConstruct;

/**
 * Created by YG on 2017-05-18.
 */
public class AuthTemplate extends BaseTemplate {

    @Autowired
    @Qualifier(value = "messageRestOperations")
    private RestOperations restOperations;

    @PostConstruct
    void init(){
        super.setRestOperations(restOperations);
    }

    public OrganigrammeResponse readOrganigramme() {
        ParameterizedTypeReference<OrganigrammeResponse> p = new ParameterizedTypeReference<OrganigrammeResponse>() {
        };
        return get(Api.AUTH_ORGANIGRAMME.getUrl() + "1/all", p);
    }

    public RoomInfoResponse getMembersInRoom(String room) {
        ParameterizedTypeReference<RoomInfoResponse> r = new ParameterizedTypeReference<RoomInfoResponse>() {
        };
        return get(Api.ROOM.getUrl() + room, r);
    }
}
