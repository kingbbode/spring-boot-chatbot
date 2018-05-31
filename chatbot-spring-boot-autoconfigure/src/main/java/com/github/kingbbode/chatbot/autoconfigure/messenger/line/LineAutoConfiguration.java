package com.github.kingbbode.chatbot.autoconfigure.messenger.line;

import com.github.kingbbode.chatbot.core.brain.DispatcherBrain;
import com.github.kingbbode.messenger.line.LineDispatcher;
import com.github.kingbbode.messenger.line.LineEventSensor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass(name = "com.github.kingbbode.messenger.line.LineDispatcher")
public class LineAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean
    public LineDispatcher lineDispatcher(DispatcherBrain dispatcherBrain) {
        return new LineDispatcher(dispatcherBrain);
    }

    @Bean
    @ConditionalOnMissingBean
    public LineEventSensor lineEventSensor(LineDispatcher lineDispatcher) {
        return new LineEventSensor(lineDispatcher);
    }
}
