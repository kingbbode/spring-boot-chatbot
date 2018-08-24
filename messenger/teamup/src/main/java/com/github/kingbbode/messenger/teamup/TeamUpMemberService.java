package com.github.kingbbode.messenger.teamup;

import com.github.kingbbode.messenger.teamup.response.OrganigrammeResponse;
import com.github.kingbbode.messenger.teamup.response.RoomInfoResponse;
import com.github.kingbbode.messenger.teamup.templates.template.AuthTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;

/**
 * Created by YG on 2017-03-31.
 */
@RequiredArgsConstructor
@Transactional
public class TeamUpMemberService {
    
    private final TeamUpMemberCached memberCached;

    private final AuthTemplate authTemplate;

    @PostConstruct
    public void update(){
        OrganigrammeResponse response = authTemplate.readOrganigramme();
        if(response == null || response.getDepartment() == null){
            return;
        }
        memberCached.updateMember(response);
    }

    public String get(String memberId){
        return memberCached.getMemberName(Long.valueOf(memberId));
    }

    public boolean contains(String memberId){
        if(!memberCached.containsMember(Long.valueOf(memberId))){
            update();
        }
        return memberCached.containsMember(Long.valueOf(memberId));
    }

    public RoomInfoResponse getRoomInfo(String room) {
        return authTemplate.getMembersInRoom(room);
    }
}
