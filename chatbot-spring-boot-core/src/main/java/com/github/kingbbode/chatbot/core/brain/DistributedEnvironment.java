package com.github.kingbbode.chatbot.core.brain;

import com.github.kingbbode.chatbot.core.common.properties.BotProperties;
import com.github.kingbbode.chatbot.core.common.request.BrainRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.Duration;
import java.util.Arrays;

@RequiredArgsConstructor
public class DistributedEnvironment {
	public static final String VALUE = "1";
	public static final Duration EXPIRE_TIME = Duration.ofMinutes(1);
	private final BotProperties botProperties;
	private final RedisTemplate<String, String> redisTemplate;

	public boolean sync(BrainRequest brainRequest) {
		String key = String.join(":", Arrays.asList(
			brainRequest.getMessenger(),
			brainRequest.getMessageNo() + botProperties.getCommandPrefix()
		));
		Boolean result = redisTemplate.opsForValue().setIfAbsent(key, VALUE, EXPIRE_TIME);
		return result == null || result;
	}
}
