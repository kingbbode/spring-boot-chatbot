package com.github.kingbbode.messenger.teamup.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Random;

/**
 * Created by jeonghoon on 2017-06-01.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class RoomInfoResponse {

    int[] users;

    public int[] getUsers() {
        return users;
    }

    public void setUsers(int[] users) {
        this.users = users;
    }

    public String pickMe() {
        return String.valueOf(this.users[new Random().nextInt(users.length)]);
    }

}
