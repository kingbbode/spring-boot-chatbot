package com.github.kingbbode.messenger.teamup.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by YG on 2017-07-21.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class RoomCreateResponse {
    private Integer room;
}
