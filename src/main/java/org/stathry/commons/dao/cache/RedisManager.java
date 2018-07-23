package org.stathry.commons.dao.cache;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * redis缓存管理服务
 *
 * @author stathry
 */
@Component("redisManager")
public class RedisManager extends AbstractRedisManager<String, Object> {

    public void set(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    public void set(String key, Object value, long timeout, TimeUnit unit) {
        redisTemplate.opsForValue().set(key, value, timeout, unit);
    }

    /** 适用于value为复杂的string格式,如JSON格式 */
    public void setString(String key, String value, TimeUnit unit, long timeout) {
        stringRedisTemplate.opsForValue().set(key, value, timeout, unit);
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

    /**
     * 如map,list 可在set后调用expire使之在指定时间之后失效
     *
     * @param key
     * @param timeout
     * @param unit
     * @return
     */
    public Boolean expire(String key, long timeout, TimeUnit unit) {
        return redisTemplate.expire(key, timeout, unit);
    }

    public <T> T get(String key) {
        return (T)redisTemplate.opsForValue().get(key);
    }

    public <HK, HV> void setMap(String key, Map<HK, HV> map) {
        if (map != null && !map.isEmpty()) {
            redisTemplate.delete(key);
            HashOperations<String, HK, HV> hashOps = redisTemplate.opsForHash();
            for (Map.Entry<HK, HV> entry : map.entrySet()) {
                hashOps.put(key, entry.getKey(), entry.getValue());
            }
        }
    }

    public <HK, HV> void addToMap(String key, Map<HK, HV> map) {
        if (map != null && !map.isEmpty()) {
            HashOperations<String, HK, HV> hashOps = redisTemplate.opsForHash();
            for (Map.Entry<HK, HV> entry : map.entrySet()) {
                hashOps.put(key, entry.getKey(), entry.getValue());
            }
        }
    }

    public <HK, HV> Map<HK, HV> getMap(String key) {
        HashOperations<String, HK, HV> hashOps = redisTemplate.opsForHash();
        return hashOps.entries(key);
    }

    public <T> void setList(String key, List<T> list) {
        if (list != null && !list.isEmpty()) {
            redisTemplate.delete(key);
            ListOperations<String, Object> listOps = redisTemplate.opsForList();
            for (T t : list) {
                listOps.rightPush(key, t);
            }
        }
    }

    public <T> void addToList(String key, List<T> list) {
        if (list != null && !list.isEmpty()) {
            ListOperations<String, Object> listOps = redisTemplate.opsForList();
            for (T t : list) {
                listOps.rightPush(key, t);
            }
        }
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> getList(String key) {
        ListOperations<String, Object> listOps = redisTemplate.opsForList();
        return (List<T>) listOps.range(key, 0, listOps.size(key));
    }

}
