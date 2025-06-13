package cn.edu.xaut.quanyou.Untils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class RedisUtil {

    @Autowired
    private RedisTemplate<String, String> stringRedisTemplate;

    private final ObjectMapper objectMapper;

    public RedisUtil() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    /**
     * 设置键值对，带过期时间
     */
    public <T> void set(String key, T value, long timeout, TimeUnit unit) {
        try {
            String jsonStr = objectMapper.writeValueAsString(value);
            stringRedisTemplate.opsForValue().set(key, jsonStr, timeout, unit);
        } catch (Exception e) {
            log.error("Redis set error: {}", e.getMessage());
        }
    }

    /**
     * 设置键值对，永不过期
     */
    public <T> void set(String key, T value) {
        try {
            String jsonStr = objectMapper.writeValueAsString(value);
            stringRedisTemplate.opsForValue().set(key, jsonStr);
        } catch (Exception e) {
            log.error("Redis set error: {}", e.getMessage());
        }
    }

    /**
     * 获取值并反序列化为指定类型
     */
    public <T> T get(String key, TypeReference<T> typeReference) {
        try {
            String jsonStr = stringRedisTemplate.opsForValue().get(key);
            if (jsonStr != null) {
                return objectMapper.readValue(jsonStr, typeReference);
            }
        } catch (Exception e) {
            log.error("Redis get error: {}", e.getMessage());
        }
        return null;
    }

    /**
     * 删除单个key
     * @param key 要删除的key
     * @return 是否删除成功
     */
    public boolean delete(String key) {
        try {
            Boolean result = stringRedisTemplate.delete(key);
            return result != null && result;
        } catch (Exception e) {
            log.error("Redis delete error for key {}: {}", key, e.getMessage());
            return false;
        }
    }

    /**
     * 批量删除key
     * @param keys 要删除的key集合
     * @return 成功删除的key数量
     */
    public long delete(Collection<String> keys) {
        try {
            if (keys == null || keys.isEmpty()) {
                return 0;
            }
            Long result = stringRedisTemplate.delete(keys);
            return result != null ? result : 0;
        } catch (Exception e) {
            log.error("Redis batch delete error: {}", e.getMessage());
            return 0;
        }
    }

    /**
     * 根据模式删除key（慎用，性能较差）
     * @param pattern 匹配模式，如 "user:*"
     * @return 成功删除的key数量
     */
    public long deleteByPattern(String pattern) {
        try {
            Set<String> keys = stringRedisTemplate.keys(pattern);
            if (keys != null && !keys.isEmpty()) {
                Long result = stringRedisTemplate.delete(keys);
                return result != null ? result : 0;
            }
            return 0;
        } catch (Exception e) {
            log.error("Redis delete by pattern error for pattern {}: {}", pattern, e.getMessage());
            return 0;
        }
    }

    /**
     * 检查key是否存在
     * @param key 要检查的key
     * @return 是否存在
     */
    public boolean exists(String key) {
        try {
            Boolean result = stringRedisTemplate.hasKey(key);
            return result != null && result;
        } catch (Exception e) {
            log.error("Redis exists check error for key {}: {}", key, e.getMessage());
            return false;
        }
    }

    /**
     * 设置key的过期时间
     * @param key 要设置过期时间的key
     * @param timeout 过期时间
     * @param unit 时间单位
     * @return 是否设置成功
     */
    public boolean expire(String key, long timeout, TimeUnit unit) {
        try {
            Boolean result = stringRedisTemplate.expire(key, timeout, unit);
            return result != null && result;
        } catch (Exception e) {
            log.error("Redis expire error for key {}: {}", key, e.getMessage());
            return false;
        }
    }

    /**
     * 获取key的剩余过期时间
     * @param key 要查询的key
     * @param unit 时间单位
     * @return 剩余过期时间，-1表示永不过期，-2表示key不存在
     */
    public long getExpire(String key, TimeUnit unit) {
        try {
            Long result = stringRedisTemplate.getExpire(key, unit);
            return result != null ? result : -2;
        } catch (Exception e) {
            log.error("Redis getExpire error for key {}: {}", key, e.getMessage());
            return -2;
        }
    }

    /**
     * 移除key的过期时间，使其永不过期
     * @param key 要操作的key
     * @return 是否操作成功
     */
    public boolean persist(String key) {
        try {
            Boolean result = stringRedisTemplate.persist(key);
            return result != null && result;
        } catch (Exception e) {
            log.error("Redis persist error for key {}: {}", key, e.getMessage());
            return false;
        }
    }
}