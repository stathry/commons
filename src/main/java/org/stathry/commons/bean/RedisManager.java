package org.stathry.commons.bean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;
import org.stathry.commons.utils.ConfigManager;

import java.security.SecureRandom;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * redis缓存管理服务
 *
 * @author stathry
 * @see <p><a href="https://yq.aliyun.com/articles/531067">阿里云Redis开发规范</a></p>
 */
@Repository
public class RedisManager {

    private static final long DEFAULT_EXPIRE_MS = ConfigManager.getObj("cache.default.expireMS.base", Long.class);
    private static final int EXPIRE_MS_BOUND = ConfigManager.getObj("cache.default.expireMS.bound", Integer.class);

    @Autowired
    protected RedisTemplate<String, Object> redisTemplate;

    @Autowired
    protected StringRedisTemplate stringRedisTemplate;

    private SecureRandom random = new SecureRandom();

    public RedisTemplate<String, Object> getRedisTemplate() {
        return redisTemplate;
    }

    public StringRedisTemplate getStringRedisTemplate() {
        return stringRedisTemplate;
    }

    private long randomExpireMS() {
        return DEFAULT_EXPIRE_MS + random.nextInt(EXPIRE_MS_BOUND);
    }

    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public String getString(String key) {
        return stringRedisTemplate.opsForValue().get(key);
    }

    public <T> List<T> get(List<String> keys) {
        return (List<T>) redisTemplate.opsForValue().multiGet(keys);
    }

    public boolean exists(String key) {
        return redisTemplate.opsForValue().get(key) != null;
    }

    // 设置过期时间为:默认过期时间+随机数
    public void set(String key, Object value) {
        redisTemplate.opsForValue().set(key, value, randomExpireMS(), TimeUnit.MILLISECONDS);
    }

    public void set(String key, Object value, long timeoutMS) {
        redisTemplate.opsForValue().set(key, value, timeoutMS, TimeUnit.MILLISECONDS);
    }

    public void set(String key, Object value, long timeout, TimeUnit unit) {
        redisTemplate.opsForValue().set(key, value, timeout, unit);
    }

    // 适用于value为JSONString,含转义字符等复杂格式
    public void setString(String key, String value, long timeout, TimeUnit unit) {
        stringRedisTemplate.opsForValue().set(key, value, timeout, unit);
    }

    public Boolean setNX(String key) {
         return setNX(key, 1, randomExpireMS());
    }

    public Boolean setNX(String key, Object value, long expireMs) {
        // spring-data-redis新版新增原子方法
        return redisTemplate.opsForValue().setIfAbsent(key, value, expireMs, TimeUnit.MILLISECONDS);
//        boolean nx = redisTemplate.opsForValue().setIfAbsent(key, value);
//        if(nx) {
//            expireRandom(key);
//        }
//        return nx;
    }

    public Boolean expireRandom(String key) {
        return redisTemplate.expire(key, randomExpireMS(), TimeUnit.MILLISECONDS);
    }

    public Boolean expire(String key, long timeout) {
        return redisTemplate.expire(key, timeout, TimeUnit.MILLISECONDS);
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
        return redisTemplate.opsForValue().increment(key, 1L);
    }

    public Long increment(String key, long delta) {
        return redisTemplate.opsForValue().increment(key, delta);
    }

    /**
     * get expire SECONDS
     */
    public Long getExpire(String key) {
        return redisTemplate.getExpire(key);
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> getList(String key) {
        ListOperations<String, Object> ops = redisTemplate.opsForList();
        return (List<T>) ops.range(key, 0, ops.size(key));
    }

    public <T> List<T> keysList(String pattern) {
        if (pattern == null || pattern.length() < 3 || pattern.startsWith("*")) {
            throw new IllegalArgumentException(pattern);
        }
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
