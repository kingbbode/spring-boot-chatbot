package com.github.kingbbode.chatbot.core.event;

import com.github.kingbbode.chatbot.core.common.properties.BotProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;

@RequiredArgsConstructor
public class DistributedEnvironment {
	private final BotProperties botProperties;
	private final RedisTemplate<String, String> redisTemplate;
}
