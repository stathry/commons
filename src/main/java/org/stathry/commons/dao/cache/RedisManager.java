package org.stathry.commons.dao.cache;

import org.springframework.data.redis.core.ListOperations;
import org.springframework.stereotype.Component;
import org.stathry.commons.utils.ConfigManager;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * redis缓存管理服务
 * @see <p><a href="https://yq.aliyun.com/articles/531067">阿里云Redis开发规范</a></p>
 *
 * @author stathry
 */
@Component("redisManager")
public class RedisManager extends AbstractRedisManager<String, Object> {

    private static final long DEFAULT_EXPIRE_MS = ConfigManager.getLong("cache.default.expireMS");
//    private static final long DEFAULT_EXPIRE_MS = 5 * 60 * 1000;

    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public <T> List<T> get(List<String> keys) {
        return (List<T>) redisTemplate.opsForValue().multiGet(keys);
    }

    public boolean exists(String key) {
        return redisTemplate.opsForValue().get(key) != null;
    }

    // 建议设置过期时间
    public void set(String key, Object value) {
        redisTemplate.opsForValue().set(key, value, DEFAULT_EXPIRE_MS, TimeUnit.MILLISECONDS);
    }

    public void set(String key, Object value, long timeout, TimeUnit unit) {
        redisTemplate.opsForValue().set(key, value, timeout, unit);
    }

    // 适用于value为JSONString,含转义字符等复杂格式
    public void setString(String key, String value, TimeUnit unit, long timeout) {
        stringRedisTemplate.opsForValue().set(key, value, timeout, unit);
    }

    public Boolean expire(String key, long timeout, TimeUnit unit) {
        return redisTemplate.expire(key, timeout, unit);
    }

    public Boolean expireAt(String key, Date time) {
        return redisTemplate.expireAt(key, time);
    }

    public void delete(String key) {
        redisTemplate.delete(key);
    }

    public void delete(Collection<String> keys) {
        redisTemplate.delete(keys);
    }

    public Long increment(String key) {
        return redisTemplate.opsForValue().increment(key, 1);
    }

    public Long increment(String key, long delta) {
        return redisTemplate.opsForValue().increment(key, delta);
    }

    // SECONDS
    public Long getExpire(String key) {
        return redisTemplate.getExpire(key);
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> getList(String key) {
        ListOperations<String, Object> ops = redisTemplate.opsForList();
        return (List<T>) ops.range(key, 0, ops.size(key));
    }

    public <T> List<T> getListByPattern(String pattern) {
        return (List<T>) redisTemplate.opsForValue().multiGet(redisTemplate.keys(pattern));
    }

    public <T> void setList(String key, List<T> list, long timeout, TimeUnit unit) {
        redisTemplate.delete(key);
        redisTemplate.opsForList().rightPushAll(key, (Collection<Object>) list);
        redisTemplate.expire(key, timeout, unit);
    }

    public <T> void addToList(String key, T t) {
        redisTemplate.opsForList().rightPush(key, t);
    }

    public <T> void addAllToList(String key, List<T> list) {
        if (list != null && !list.isEmpty()) {
            redisTemplate.opsForList().rightPushAll(key, (Collection<Object>) list);
        }
    }

    public <HK, HV> Map<HK, HV> getMap(String key) {
        return (Map<HK, HV>) redisTemplate.opsForHash().entries(key);
    }

    public <HK, HV> void setMap(String key, Map<HK, HV> map, long timeout, TimeUnit unit) {
        redisTemplate.delete(key);
        redisTemplate.opsForHash().putAll(key, map);
        redisTemplate.expire(key, timeout, unit);
    }

    public <HK, HV> void putToMap(String key, Map<HK, HV> map) {
        redisTemplate.opsForHash().putAll(key, map);
    }

}
