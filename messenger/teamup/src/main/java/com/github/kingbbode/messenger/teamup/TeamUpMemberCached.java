package com.github.kingbbode.messenger.teamup;

import com.github.kingbbode.messenger.teamup.response.OrganigrammeResponse;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by YG on 2017-04-05.
 */
public class TeamUpMemberCached {
    //id,name
    private Map<Long, String> members;
    
    public void updateMember(OrganigrammeResponse organigramme){
        Map<Long, String> tmpMembers = new HashMap<>();
        organigramme.getDepartment()
                .forEach(department -> findUsers(department, tmpMembers));
        members = tmpMembers;
    }
    
    private void findUsers(OrganigrammeResponse.Department department, Map<Long, String> tmpMembers){
        if(department == null){
            return;
        }
        if(department.getUsers() != null) {
            department.getUsers().forEach(user -> user.takeInfo(tmpMembers));
        }
        if(department.getDepartment() != null){
            department.getDepartment().forEach(department1 -> findUsers(department1, tmpMembers));
        }
    }
    
    public String getMemberName(Long id){
        return members.get(id);
    }
    
    public boolean containsMember(Long id){
        return members.containsKey(id);
    }
}
