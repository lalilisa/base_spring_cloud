package com.trimv.base.service.command;



import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
@Service
public class LockService {

    private static final String LOCK_KEY = "MKC_CORE_WS_LOCK";

    private RedisTemplate<String, String> redisTemplate;

//    private AppConf appConf;

    private String nodeId;

    @PostConstruct
    void init() {
        nodeId = nodeId + "-" + "appConf.getInstanceId()";
    }


    public boolean tryLock(String name, Long leaseTime, Long renewalPoint) {
        String key = "STK_LOCK_" + name;
        try {
            String holder = redisTemplate.opsForValue().get(key);
            if (holder != null) {
                if (holder.equals(nodeId)) {
                    Long ttl = redisTemplate.getExpire(key, TimeUnit.MILLISECONDS);
                    if (ttl == null || (renewalPoint != null && ttl < renewalPoint)) { // renew lock expiration
                        redisTemplate.expire(key, Duration.ofMillis(leaseTime));
                    }
                    return true;
                } else {
//                    log.warn("Current locked by {}", holder);
                }
                return false;
            } else {
                redisTemplate.opsForValue().set(key, nodeId);
                redisTemplate.expire(key, Duration.ofMillis(leaseTime));
                // In case more than 2 instances desire to lock, we have to recheck which one successfully held the lock
                holder = redisTemplate.opsForValue().get(key);
                if (holder != null && holder.equals(nodeId)) {
                    Long ttl = redisTemplate.getExpire(key, TimeUnit.MILLISECONDS);
                    if (ttl == null || (renewalPoint != null && ttl < renewalPoint)) {
                        redisTemplate.expire(key, Duration.ofMillis(leaseTime));
                    }
                    return true;
                } else {
//                    log.warn("Current locked by {}", holder);
                    return false;
                }
            }

        } catch (Throwable t) {
            log.error("Refuse to handle lock service, probably redis error", t);
            return false;
        }
    }

    public String ttl() {
        return "" + redisTemplate.getExpire(LOCK_KEY);
    }

    public boolean tryLockSpinReq(String name, Long leaseTime, Long renewalPoint, Long miliseconds) {
        String key = "STK_LOCK_" + name;
        try {
            String holder = redisTemplate.opsForValue().get(key);
            if (holder != null) {
                if (holder.equals(nodeId + miliseconds.toString())) {
                    Long ttl = redisTemplate.getExpire(key, TimeUnit.MILLISECONDS);
                    if (ttl == null || (renewalPoint != null && ttl < renewalPoint)) { // renew lock expiration
                        redisTemplate.expire(key, Duration.ofMillis(leaseTime));
                    }
                    return true;
                } else {
//                    log.warn("Current locked by {}", holder);
                }
                return false;
            } else {
                redisTemplate.opsForValue().set(key, nodeId + miliseconds.toString());
                redisTemplate.expire(key, Duration.ofMillis(leaseTime));
                // In case more than 2 instances desire to lock, we have to recheck which one successfully held the lock
                holder = redisTemplate.opsForValue().get(key);
                if (holder != null && holder.equals(nodeId + miliseconds)) {
                    Long ttl = redisTemplate.getExpire(key, TimeUnit.MILLISECONDS);
                    if (ttl == null || (renewalPoint != null && ttl < renewalPoint)) {
                        redisTemplate.expire(key, Duration.ofMillis(leaseTime));
                    }
                    return true;
                } else {
//                    log.warn("Current locked by {}", holder);
                    return false;
                }
            }

        } catch (Throwable t) {
            log.error("Refuse to handle lock service, probably redis error", t);
            return false;
        }
    }
    public void release(String key) {
        redisTemplate.delete(key);
    }
}