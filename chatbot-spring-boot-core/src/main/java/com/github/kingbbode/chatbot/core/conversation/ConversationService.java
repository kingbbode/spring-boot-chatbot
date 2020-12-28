package com.github.kingbbode.chatbot.core.conversation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.kingbbode.chatbot.core.common.properties.BotProperties;
import lombok.RequiredArgsConstructor;
import org.joda.time.DateTime;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.PostConstruct;
import java.io.IOException;

/**
 * Created by YG on 2017-04-03.
 */
@RequiredArgsConstructor
public class ConversationService {
    private final Environment environment;
    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;
    private final BotProperties botProperties;

    private static final String REDIS_KEY = ":conversation:";

    private String key;
    private int expireTime;

    @PostConstruct
    private void init() {
        this.key = botProperties.isTestMode() ? "test:" + botProperties.getName() + REDIS_KEY : botProperties.getName() + REDIS_KEY;
        this.expireTime = environment.acceptsProfiles("dev") ? 300 : 30;
    }


    public Conversation pop(String userId) throws IOException {
        String result = redisTemplate.opsForList().rightPop(this.key + userId);
        if (result != null) {
            redisTemplate.expireAt(this.key + userId, new DateTime().plusSeconds(expireTime).toDate());
        }
        return result != null ? objectMapper.readValue(redisTemplate.opsForList().rightPop(this.key + userId), Conversation.class) : null;
    }

    public void push(String userId, Conversation value) throws JsonProcessingException {
        redisTemplate.opsForList().rightPush(this.key + userId, objectMapper.writeValueAsString(value));
        this.touch(userId);
    }

    public void touch(String userId) {
        redisTemplate.expireAt(this.key + userId, new DateTime().plusSeconds(expireTime).toDate());
    }

    public Conversation getLatest(String userId) throws IOException {
        Long size = redisTemplate.opsForList().size(this.key + userId);
        String result = redisTemplate.opsForList().index(this.key + userId, (size == null ? 0 : size) - 1);
        if (result != null) {
            redisTemplate.expireAt(this.key + userId, new DateTime().plusSeconds(expireTime).toDate());
        }
        return result != null ? objectMapper.readValue(result, Conversation.class) : null;
    }

    public void delete(String userId) {
        redisTemplate.opsForValue().getOperations().delete(this.key + userId);
    }
}
