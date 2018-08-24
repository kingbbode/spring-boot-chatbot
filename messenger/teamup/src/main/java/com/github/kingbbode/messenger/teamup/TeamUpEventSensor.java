package com.github.kingbbode.messenger.teamup;

import com.github.kingbbode.chatbot.core.common.interfaces.EventSensor;
import com.github.kingbbode.chatbot.core.event.Event;
import com.github.kingbbode.chatbot.core.event.EventQueue;
import com.github.kingbbode.messenger.teamup.response.EventResponse;
import com.github.kingbbode.messenger.teamup.templates.template.EventTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ObjectUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by YG on 2016-03-28.
 */
@Slf4j
@RequiredArgsConstructor
public class TeamUpEventSensor implements EventSensor {

    private final EventTemplate eventTemplate;

    private final EventQueue eventQueue;
    
    private final TeamUpDispatcher teamUpDispatcher;

    private boolean ready;
    
    public void setReady(boolean ready){
        this.ready = ready;
    }

    @Override
    public List<Event> sensingEvent(){
        if(ready) {
            EventResponse eventResponse = null;
            try {
                eventResponse = eventTemplate.getEvent();
            } catch (Exception e) {
                log.error("TeamUpEventSensor - sensingEvent : {}", e);
            }
            if (!ObjectUtils.isEmpty(eventResponse)) {
                return eventResponse.getEvents().stream()
                        .map(event ->  new Event<>(teamUpDispatcher, event))
                        .collect(Collectors.toList());
            }
        }
        return Collections.emptyList();
    }
}
