package com.github.kingbbode.chatbot.core.conversation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.kingbbode.chatbot.core.common.properties.BotProperties;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.IOException;

/**
 * Created by YG on 2017-04-03.
 */
public class ConversationService {

    @Autowired
    private Environment environment;

    @Resource(name = "redisTemplate")
    private RedisTemplate<String, String> redisTemplate;

    @Resource(name = "redisTemplate")
    private ListOperations<String, String> listOperations;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BotProperties botProperties;

    private static final String REDIS_KEY = ":conversation:";

    private String key;
    private int expireTime;

    @PostConstruct
    private void init() {
        this.key = botProperties.isTestMode() ? "test:" + botProperties.getName() + REDIS_KEY : botProperties.getName() + REDIS_KEY;
        this.expireTime = environment.acceptsProfiles("dev") ? 300 : 30;
    }


    public Conversation pop(String userId) throws IOException {
        String result = listOperations.rightPop(this.key + userId);
        if (result != null) {
            redisTemplate.expireAt(this.key + userId, new DateTime().plusSeconds(expireTime).toDate());
        }
        return result != null ? objectMapper.readValue(listOperations.rightPop(this.key + userId), Conversation.class) : null;
    }

    public void push(String userId, Conversation value) throws JsonProcessingException {
        listOperations.rightPush(this.key + userId, objectMapper.writeValueAsString(value));
        this.touch(userId);
    }

    public void touch(String userId) {
        redisTemplate.expireAt(this.key + userId, new DateTime().plusSeconds(expireTime).toDate());
    }

    public Conversation getLatest(String userId) throws IOException {
        String result = listOperations.index(this.key + userId, listOperations.size(this.key + userId) - 1);
        if (result != null) {
            redisTemplate.expireAt(this.key + userId, new DateTime().plusSeconds(expireTime).toDate());
        }
        return result != null ? objectMapper.readValue(result, Conversation.class) : null;
    }

    public void delete(String userId) {
        redisTemplate.opsForValue().getOperations().delete(this.key + userId);
    }
}
