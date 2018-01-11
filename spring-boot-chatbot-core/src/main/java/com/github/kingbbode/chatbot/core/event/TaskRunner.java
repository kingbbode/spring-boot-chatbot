package com.github.kingbbode.chatbot.core.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

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

    @Scheduled(fixedDelay = 10)
    private void execute(){
        while(eventQueue.hasNext()){
            executer.execute(new FetcherTask(eventQueue.poll()));
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
}
