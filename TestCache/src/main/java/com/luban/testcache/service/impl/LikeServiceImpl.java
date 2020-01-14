package com.luban.testcache.service.impl;

import com.luban.testcache.entity.Talk;
import com.luban.testcache.service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

@Service
public class LikeServiceImpl implements LikeService {

    @Autowired
    RedisTemplate redisTemplate;

    @Override
    public boolean likeAndCancelLike(Talk talk) {
        boolean execute = true;
        try {
            redisTemplate.execute(new RedisCallback<Boolean>() {

                @Nullable
                @Override
                public Boolean doInRedis(RedisConnection redisConnection) throws DataAccessException {
                    Boolean aBoolean = redisConnection.setBit(talk.getId().getBytes(), talk.getLikeUserId(), talk.isStatus());
                    redisConnection.close();
                    return aBoolean;
                }
            });
        }catch (Exception e){
            execute=false;
        }
        return execute;
    }


    @Override
    public long getLikeCount(Talk talk) {
        Object execute = redisTemplate.execute(new RedisCallback<Long>() {

            @Nullable
            @Override
            public Long doInRedis(RedisConnection redisConnection) throws DataAccessException {
                Long aLong = redisConnection.bitCount(talk.getId().getBytes());
                redisConnection.close();
                return aLong;
            }
        });
        return (long) execute;
    }

    @Override
    public boolean isLike(Talk talk) {
        return (boolean) redisTemplate.execute(new RedisCallback<Boolean>() {
            @Nullable
            @Override
            public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                Boolean bit = connection.getBit(talk.getId().getBytes(), talk.getLikeUserId());
                connection.close();
                return bit;
            }
        });
    }
}
