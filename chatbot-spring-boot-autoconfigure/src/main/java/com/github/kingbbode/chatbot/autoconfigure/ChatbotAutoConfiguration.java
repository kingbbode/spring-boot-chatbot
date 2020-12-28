package com.github.kingbbode.chatbot.autoconfigure;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.kingbbode.chatbot.core.ChatbotProperties;
import com.github.kingbbode.chatbot.core.base.BaseBrain;
import com.github.kingbbode.chatbot.core.base.knowledge.brain.KnowledgeBrain;
import com.github.kingbbode.chatbot.core.base.knowledge.component.KnowledgeComponent;
import com.github.kingbbode.chatbot.core.base.stat.StatComponent;
import com.github.kingbbode.chatbot.core.brain.DispatcherBrain;
import com.github.kingbbode.chatbot.core.brain.DistributedEnvironment;
import com.github.kingbbode.chatbot.core.brain.aop.BrainCellAspect;
import com.github.kingbbode.chatbot.core.brain.factory.BrainFactory;
import com.github.kingbbode.chatbot.core.brain.factory.BrainFactoryCustomizer;
import com.github.kingbbode.chatbot.core.common.interfaces.EventSensor;
import com.github.kingbbode.chatbot.core.common.properties.BotProperties;
import com.github.kingbbode.chatbot.core.conversation.ConversationService;
import com.github.kingbbode.chatbot.core.event.EventQueue;
import com.github.kingbbode.chatbot.core.event.TaskRunner;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.client.RestOperations;
import redis.embedded.RedisServer;

import java.io.IOException;
import java.util.List;

import static com.github.kingbbode.chatbot.core.common.util.RestTemplateFactory.getRestOperations;

/**
 * Created by YG on 2017-07-10.
 */
@Configuration
@ConditionalOnProperty(prefix = "chatbot", name = "enabled", havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties({RedisProperties.class, ChatbotProperties.class})
@EnableScheduling
public class ChatbotAutoConfiguration {

    public static final String EVENT_QUEUE_TREAD_POOL = "eventQueueTreadPool";
    private ChatbotProperties chatbotProperties;

    public ChatbotAutoConfiguration(ChatbotProperties chatbotProperties) {
        this.chatbotProperties = chatbotProperties;
    }

    @Bean
    @ConditionalOnMissingBean
    public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, String> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        return template;
    }

    @Bean
    @ConditionalOnMissingBean
    public BrainFactory brainFactory(
        BotProperties botProperties,
        BeanFactory beanFactory,
        @Autowired(required = false) KnowledgeComponent knowledge,
        ChatbotProperties chatbotProperties,
        List<BrainFactoryCustomizer> brainFactoryCustomizers
    ){
        return new BrainFactory(botProperties, beanFactory, knowledge, chatbotProperties, brainFactoryCustomizers);
    }

    @Bean
    @ConditionalOnMissingBean
    public EventQueue eventQueue(){
        return new EventQueue();
    }

    @Bean(name = "embeddedRedis", destroyMethod = "stop")
    @ConditionalOnProperty(name = "chatbot.enableEmbeddedRedis", havingValue = "true", matchIfMissing = true)
    public RedisServer redisServer(RedisProperties redisProperties) throws IOException {
        RedisServer redisServer = new RedisServer(redisProperties.getPort());
        redisServer.start();
        return redisServer;
    }

    @Bean
    @ConditionalOnMissingBean
    public ConversationService conversationService(
        Environment environment,
        RedisTemplate<String, String> redisTemplate,
        ObjectMapper objectMapper,
        BotProperties botProperties
    ){
        return new ConversationService(environment, redisTemplate, objectMapper, botProperties);
    }

    @Bean
    @Primary
    public RestOperations messageRestOperations() {
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setConnectTimeout(1000);
        factory.setReadTimeout(1000);
        return getRestOperations(factory);
    }

    @Bean(name = EVENT_QUEUE_TREAD_POOL)
    public ThreadPoolTaskExecutor eventQueueTreadPool() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(1000);
        executor.setWaitForTasksToCompleteOnShutdown(true);
        return executor;
    }

    @Bean
    @ConditionalOnMissingBean
    public DistributedEnvironment distributedEnvironment(
        BotProperties botProperties,
        RedisTemplate<String, String> redisTemplate
    ) {
        return new DistributedEnvironment(botProperties, redisTemplate);
    }

    @Bean
    @ConditionalOnMissingBean
    public TaskRunner taskRunner(
        @Qualifier(EVENT_QUEUE_TREAD_POOL) ThreadPoolTaskExecutor executer,
        EventQueue eventQueue,
        DispatcherBrain dispatcherBrain,
        List<EventSensor> eventSensors
    ){
        return new TaskRunner(executer, eventQueue, eventSensors, dispatcherBrain);
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "chatbot", name = "enableBase", havingValue = "true", matchIfMissing = true)
    public BaseBrain baseBrain(
        BrainFactory brainFactory,
        @Autowired(required = false) KnowledgeComponent knowledgeComponent,
        StatComponent statComponent
    ){
        return new BaseBrain(brainFactory, knowledgeComponent, statComponent);
    }

    @Bean
    @ConditionalOnMissingBean    
    @ConditionalOnProperty(prefix = "chatbot", name = "enableKnowledge", havingValue = "true")
    public KnowledgeBrain knowledgeBrain(KnowledgeComponent knowledgeComponent){
        return new KnowledgeBrain(knowledgeComponent);
    }
    
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "chatbot", name = "enableKnowledge", havingValue = "true")
    public KnowledgeComponent knowledgeComponent(
        RedisTemplate<String, String> redisTemplate,
        ObjectMapper objectMapper,
        BotProperties botProperties,
        StatComponent statComponent
    ){
        return new KnowledgeComponent(redisTemplate, objectMapper, botProperties, statComponent);
    }
    
    @Bean
    @ConditionalOnMissingBean
    public StatComponent statComponent(){
        return new StatComponent();
    }

    @Bean
    @Profile({"bot"})
    public BotProperties realBotConfig(){
        return new BotProperties(chatbotProperties.getName(), chatbotProperties.getCommandPrefix(), false);
    }

    @Bean
    @Profile("!bot")
    public BotProperties devBotConfig(){
        return new BotProperties(chatbotProperties.getName(),"#" +  chatbotProperties.getCommandPrefix(), true);
    }
    
    @Bean
    @ConditionalOnMissingBean
    public DispatcherBrain dispatcherBrain(
        BrainFactory brainFactory,
        ConversationService conversationService,
        DistributedEnvironment distributedEnvironment
    ){
        return new DispatcherBrain(brainFactory, conversationService, distributedEnvironment);
    }

    @Bean
    @ConditionalOnMissingBean
    public BrainCellAspect brainCellAspect(
        ConversationService conversationService,
        BrainFactory brainFactory,
        StatComponent statComponent
    ){
        return new BrainCellAspect(conversationService, brainFactory, statComponent);
    }
}
