package com.github.kingbbode.messenger.teamup.request;

/**
 * Created by YG on 2017-07-21.
 */
public class RoomCreateRequest {
    public RoomCreateRequest(int[] users) {
        this.users = users;
    }

    private int[] users;

    public int[] getUsers() {
        return users;
    }

    public void setUsers(int[] users) {
        this.users = users;
    }
}
