package com.github.kingbbode.chatbot.core.event;

import com.github.kingbbode.chatbot.core.brain.DispatcherBrain;
import com.github.kingbbode.chatbot.core.common.interfaces.EventSensor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created by YG on 2016-08-17.
 */
@Slf4j
@Service
@ConditionalOnProperty(prefix = "chatbot", name = "enabled", havingValue = "true")
public class TaskRunner {
    
    @Resource(name = "eventQueueTreadPool")
    private ThreadPoolTaskExecutor executer;

    private final EventQueue eventQueue;

    private final List<EventSensor> eventSensors;

    private final DispatcherBrain brain;

    @Autowired
    public TaskRunner(EventQueue eventQueue, List<EventSensor> eventSensors, DispatcherBrain brain) {
        this.eventQueue = eventQueue;
        this.eventSensors = Optional.ofNullable(eventSensors).orElse(Collections.emptyList());
        this.brain = brain;
    }

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
            try {
                this.event.execute();
            } catch (Exception e) {
                log.warn("execute error message={}", e.getMessage(), e);
            }
        }
    }

    @Scheduled(fixedDelay = 10)
    public void sensingEvent(){
        eventSensors.stream()
                .map(EventSensor::sensingEvent)
                .flatMap(Collection::stream)
                .forEach(this.eventQueue::offer);
    }
}
