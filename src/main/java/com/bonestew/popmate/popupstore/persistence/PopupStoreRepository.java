package com.bonestew.popmate.popupstore.persistence;

import java.util.Dictionary;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PopupStoreRepository {

    private final RedisTemplate<String, Object> redisTemplate;

    public void createUserViewedKey(Long popupStoreId, Long userId) {
        String userViewKey = "USER:" + userId + "POST:" + popupStoreId;
        redisTemplate.opsForValue().set(userViewKey, true);
        redisTemplate.expire(userViewKey, 1, TimeUnit.DAYS);
    }

    public Boolean hasKey(Long popupStoreId, Long userId) {
        String userKey = "USER:" + userId + "POST:" + popupStoreId;
        return redisTemplate.hasKey(userKey);
    }

    public void incrementPostView(Long popupStoreId, Long views) {
        String postKey = "POST:" + popupStoreId;
        Boolean keyExists = redisTemplate.hasKey(postKey);
        if (keyExists && keyExists != null) {
            redisTemplate.opsForValue().increment(postKey);
        } else {
            redisTemplate.opsForValue().set(postKey, views + 1L);
        }
    }

    public Set<String> getKeys(String s) {
        return redisTemplate.keys(s);
    }

    public Long getViews(String key) {
        return Long.parseLong((String) redisTemplate.opsForValue().get(key));
    }

    public void removeKey(String key) {
        redisTemplate.delete(key);

    }
}
