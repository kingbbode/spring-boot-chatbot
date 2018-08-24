package com.github.kingbbode.messenger.teamup;

import com.github.kingbbode.chatbot.core.event.Event;
import com.github.kingbbode.chatbot.core.event.EventQueue;
import com.github.kingbbode.messenger.teamup.response.EventResponse;
import com.github.kingbbode.messenger.teamup.templates.template.EventTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;

/**
 * Created by YG on 2016-03-28.
 */
@Slf4j
@RequiredArgsConstructor
public class TeamUpEventSensor {

    private final EventTemplate eventTemplate;

    private final EventQueue eventQueue;
    
    private final TeamUpDispatcher teamUpDispatcher;

    private boolean ready;
    
    public void setReady(boolean ready){
        this.ready = ready;
    }

    @Scheduled(fixedDelay = 10)
    public void sensingEvent(){
        if(ready) {
            EventResponse eventResponse = null;
            try {
                eventResponse = eventTemplate.getEvent();
            } catch (Exception e) {
                log.error("TeamUpEventSensor - sensingEvent : {}", e);
            }
            if (!ObjectUtils.isEmpty(eventResponse)) {
                ArrayList<EventResponse.Event> events = eventResponse.getEvents();
                if (events != null && !events.isEmpty()) {
                    events.forEach(event -> this.eventQueue.offer(
                            new Event<>(teamUpDispatcher, event)
                    ));
                }
            }
        }
    }
}
