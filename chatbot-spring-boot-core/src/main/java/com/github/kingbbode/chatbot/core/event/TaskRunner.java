package com.github.kingbbode.chatbot.core.event;

import com.github.kingbbode.chatbot.core.brain.DispatcherBrain;
import com.github.kingbbode.chatbot.core.common.interfaces.EventSensor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.Collection;
import java.util.List;

/**
 * Created by YG on 2016-08-17.
 */
@Slf4j
@RequiredArgsConstructor
public class TaskRunner {

    private final ThreadPoolTaskExecutor executer;
    private final EventQueue eventQueue;
    private final List<EventSensor> eventSensors;
    private final DispatcherBrain brain;

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
