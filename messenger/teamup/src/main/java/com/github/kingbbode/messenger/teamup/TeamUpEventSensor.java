package com.github.kingbbode.messenger.teamup;

import com.github.kingbbode.chatbot.core.event.Event;
import com.github.kingbbode.chatbot.core.event.EventQueue;
import com.github.kingbbode.messenger.teamup.response.EventResponse;
import com.github.kingbbode.messenger.teamup.templates.template.EventTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;

/**
 * Created by YG on 2016-03-28.
 */
public class TeamUpEventSensor {

    private static final Logger logger = LoggerFactory.getLogger( TeamUpEventSensor.class );
        
    @Autowired
    private EventTemplate eventTemplate;
    
    @Autowired
    private EventQueue eventQueue;
    
    @Autowired
    private TeamUpDispatcher teamUpDispatcher;

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
                logger.error("TeamUpEventSensor - sensingEvent : {}", e);
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
