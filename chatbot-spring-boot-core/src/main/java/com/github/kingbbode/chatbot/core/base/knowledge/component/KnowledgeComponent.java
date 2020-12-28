package com.github.kingbbode.chatbot.core.base.knowledge.component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.kingbbode.chatbot.core.base.stat.StatComponent;
import com.github.kingbbode.chatbot.core.common.properties.BotProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by YG on 2016-04-12.
 */
@RequiredArgsConstructor
public class KnowledgeComponent {

    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;
    private final BotProperties botProperties;
    private final StatComponent statComponent;

    private static final String REDIS_KEY_KNOWLEDGE = ":knowledge";

    private String key;
    private Map<String, List<String>> knowledge;
    
    @PostConstruct
    public void init() throws IOException {
        this.key = botProperties.getName() + REDIS_KEY_KNOWLEDGE;
        Map<String, List<String>> map = new ConcurrentHashMap<>();
        HashOperations<String, String, String> hashOperations = redisTemplate.opsForHash();
        Map<String, String> entries = hashOperations.entries(this.key);
        for(Map.Entry<String, String> entry : entries.entrySet()){
            map.put(entry.getKey(), objectMapper.readValue(entry.getValue(), new TypeReference<List<String>>() {
            }));
        }
        knowledge = map;
    }

    public void clear() {
        knowledge.clear();
    }

    public boolean contains(String command){
        return knowledge.containsKey(command);
    }

    public List<String> get(String command) {
        List<String> candi = knowledge.getOrDefault(command, null);
        if(candi != null){
            statComponent.plus("학습:" + command);
        }
        return candi;
    }

    public String addKnowledge(String command, String content) throws JsonProcessingException {
        if (content.startsWith(botProperties.getCommandPrefix())) {
            return botProperties.getCommandPrefix() + "로 시작하는 내용은 암기할 수 없습니다";
        }

        if (knowledge.containsKey(command)) {
            List<String> list = knowledge.get(command);
            if (list.size() > 9) {
                return command + " 명령어에 습득할 수 있는 머리가 꽉 차서 못하겠습니다";
            }
            list.add(content);
            redisTemplate.opsForHash().put(this.key, command, objectMapper.writeValueAsString(list));
            return command + " 명령어에 지식을 하나 더 습득했습니다";
        } else {
            if (knowledge.size() > 10000) {
                return "제 머리로는 더 이상 지식을 습득할 수 없습니다";
            }
            List<String> list = new ArrayList<>();
            list.add(content);
            knowledge.put(command, list);
            redisTemplate.opsForHash().put(this.key, command, objectMapper.writeValueAsString(list));
            return "새로운 지식을 습득했습니다. \n 사용법 : " + command;
        }
    }

    public String forgetKnowledge(String command) {
        if (knowledge.containsKey(command)) {
            knowledge.remove(command);
            redisTemplate.opsForHash().delete(this.key, command);
            return command + "를 까먹었습니다";
        } else {
            return "그런건 원래 모릅니다";
        }
    }
    
    public Set<Map.Entry<String, List<String>>> getCommands(){
        return knowledge.entrySet();
    }
}
