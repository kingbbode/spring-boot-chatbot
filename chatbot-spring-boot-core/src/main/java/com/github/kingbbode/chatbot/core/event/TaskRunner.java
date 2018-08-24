package com.github.kingbbode.chatbot.core.event;

import com.github.kingbbode.chatbot.core.brain.DispatcherBrain;
import com.github.kingbbode.chatbot.core.common.interfaces.EventSensor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by YG on 2016-08-17.
 */
@Service
@EnableScheduling
@ConditionalOnProperty(prefix = "chatbot", name = "enabled", havingValue = "true")
public class TaskRunner {
    
    @Resource(name = "eventQueueTreadPool")
    private ThreadPoolTaskExecutor executer;

    @Autowired
    private EventQueue eventQueue;

    @Autowired
    private List<EventSensor> eventSensors;

    @Autowired
    private DispatcherBrain brain;

    @Scheduled(fixedDelay = 10)
    private void execute(){
        while(eventQueue.hasNext()){
            Event event = eventQueue.poll();
            event.setBrain(brain);
            executer.execute(new FetcherTask(event));
        }
    }
    
    public static class FetcherTask implements Runnable {
        Event event;
        FetcherTask(Event event) {
            this.event = event;
        }

        @Override
        public void run() {
            this.event.execute();
        }
    }

    @Scheduled(fixedDelay = 10)
    public void sensingEvent(){
        eventSensors.stream()
                .map(EventSensor::sensingEvent)
                .flatMap(Collection::stream)
                .forEach(event -> this.eventQueue.offer(event));
    }
}
