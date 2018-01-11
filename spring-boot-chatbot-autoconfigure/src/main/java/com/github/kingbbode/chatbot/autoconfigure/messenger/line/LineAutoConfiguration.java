package com.github.kingbbode.chatbot.autoconfigure.messenger.line;

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
    public LineDispatcher lineDispatcher() {
        return new LineDispatcher();
    }

    @Bean
    @ConditionalOnMissingBean
    public LineEventSensor lineEventSensor() {
        return new LineEventSensor();
    }
}
