package com.github.kingbbode.chatbot.autoconfigure;

import com.github.kingbbode.chatbot.core.ChatbotProperties;
import com.github.kingbbode.chatbot.core.base.BaseBrain;
import com.github.kingbbode.chatbot.core.base.knowledge.brain.KnowledgeBrain;
import com.github.kingbbode.chatbot.core.base.knowledge.component.KnowledgeComponent;
import com.github.kingbbode.chatbot.core.base.stat.StatComponent;
import com.github.kingbbode.chatbot.core.brain.DispatcherBrain;
import com.github.kingbbode.chatbot.core.brain.aop.BrainCellAspect;
import com.github.kingbbode.chatbot.core.brain.factory.BrainFactory;
import com.github.kingbbode.chatbot.core.common.properties.BotProperties;
import com.github.kingbbode.chatbot.core.common.util.RestTemplateFactory;
import com.github.kingbbode.chatbot.core.conversation.ConversationService;
import com.github.kingbbode.chatbot.core.event.EventQueue;
import com.github.kingbbode.chatbot.core.event.TaskRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;
import redis.embedded.RedisServer;

import java.io.IOException;

import static com.github.kingbbode.chatbot.core.common.util.RestTemplateFactory.getRestOperations;

/**
 * Created by YG on 2017-07-10.
 */
@Configuration
@ConditionalOnProperty(prefix = "chatbot", name = "enabled", havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties(ChatbotProperties.class)
public class ChatbotAutoConfiguration {
    
    private ChatbotProperties chatbotProperties;

    public ChatbotAutoConfiguration(ChatbotProperties chatbotProperties) {
        this.chatbotProperties = chatbotProperties;
    }

    @Bean
    @ConditionalOnMissingBean
    public BrainFactory brainFactory(){
        return new BrainFactory();
    }

    @Bean
    @ConditionalOnMissingBean
    public EventQueue eventQueue(){
        return new EventQueue();
    }

    @Bean(name = "embeddedRedis", destroyMethod = "stop")
    @ConditionalOnBean
    @ConditionalOnProperty(name = "chatbot.useExternalRedis", havingValue = "true", matchIfMissing = true)
    public RedisServer redisServer() throws IOException {
        RedisServer redisServer = new RedisServer(chatbotProperties.getPort());
        redisServer.start();
        return redisServer;
    }

    @Bean
    @ConditionalOnMissingBean
    @DependsOn("embeddedRedis")
    public JedisConnectionFactory jedisConnectionFactory() {
        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory();
        jedisConnectionFactory.setHostName(chatbotProperties.getHostName());
        jedisConnectionFactory.setPort(chatbotProperties.getPort());
        jedisConnectionFactory.setTimeout(chatbotProperties.getTimeout());
        jedisConnectionFactory.setPassword(chatbotProperties.getPassword());
        jedisConnectionFactory.setUsePool(chatbotProperties.isUsePool());
        jedisConnectionFactory.setUseSsl(chatbotProperties.isUseSsl());
        jedisConnectionFactory.setDatabase(chatbotProperties.getDbIndex());
        jedisConnectionFactory.setClientName(chatbotProperties.getClientName());
        jedisConnectionFactory.setConvertPipelineAndTxResults(chatbotProperties.isConvertPipelineAndTxResults());

        return jedisConnectionFactory;
    }
    
    @Bean
    @ConditionalOnMissingBean
    public StringRedisTemplate redisTemplate(JedisConnectionFactory jedisConnectionFactory) {
        StringRedisTemplate stringRedisTemplate = new StringRedisTemplate();
        stringRedisTemplate.setConnectionFactory(jedisConnectionFactory);
        return stringRedisTemplate;
    }

    @Bean
    @ConditionalOnMissingBean
    public ConversationService conversationService(){
        return new ConversationService();
    }

    @Bean(name = "messageRestOperations")
    public RestOperations messageRestOperations() {
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setConnectTimeout(1000);
        factory.setReadTimeout(1000);
        return getRestOperations(factory);
    }

    @Bean(name = "fileRestOperations")
    public RestOperations fileRestOperations() {
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setConnectTimeout(20000);
        factory.setReadTimeout(20000);
        RestTemplate restTemplate = (RestTemplate) RestTemplateFactory.getRestOperations(factory);
        restTemplate.getMessageConverters().add(new ByteArrayHttpMessageConverter());
        return restTemplate;
    }

    @Bean(name = "eventRestOperations")
    public RestOperations eventRestOperations() {
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setConnectTimeout(30000);
        factory.setReadTimeout(30000);
        return getRestOperations(factory);
    }

    @Bean(name = "eventQueueTreadPool")
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
    public TaskRunner taskRunner(){
        return new TaskRunner();
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "chatbot", name = "enableBase", havingValue = "true", matchIfMissing = true)
    public BaseBrain baseBrain(){
        return new BaseBrain();
    }

    @Bean
    @ConditionalOnMissingBean    
    @ConditionalOnProperty(prefix = "chatbot", name = "enableKnowledge", havingValue = "true")
    public KnowledgeBrain knowledgeBrain(){
        return new KnowledgeBrain();
    }
    
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "chatbot", name = "enableKnowledge", havingValue = "true")
    public KnowledgeComponent knowledgeComponent(){
        return new KnowledgeComponent();
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
    @Profile({"dev"})
    public BotProperties devBotConfig(){
        return new BotProperties(chatbotProperties.getName(),"#" +  chatbotProperties.getCommandPrefix(), true);
    }
    
    @Bean
    @ConditionalOnMissingBean
    public DispatcherBrain dispatcherBrain(){
        return new DispatcherBrain();
    }

    @Bean
    @ConditionalOnMissingBean
    public BrainCellAspect brainCellAspect(){
        return new BrainCellAspect();
    }
}
