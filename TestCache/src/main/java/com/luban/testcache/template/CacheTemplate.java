package com.luban.testcache.template;

import com.luban.testcache.entity.NullValueResultDO;
import com.luban.testcache.entity.Order;
import com.luban.testcache.entity.R;
import com.luban.testcache.filter.RedisBloomFilter;
import com.luban.testcache.mapper.OrderMapper;
import com.luban.testcache.util.RedisLock;
import org.redisson.Redisson;
import org.redisson.RedissonMultiLock;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class CacheTemplate<T> {

    @Autowired
    private ValueOperations<String,Object> valueOperations;

    @Autowired
    RedisBloomFilter bloomFilter;


    @Autowired
    private RedisLock redisLock;

    @Autowired
    private Redisson redisson;

    @Autowired
    OrderMapper orderMapper;



    public R findCache(String key, long expire, TimeUnit unit, CacheLoadble<T> cacheLoadble){
        //查询缓存
        Object redisObj = valueOperations.get(String.valueOf(key));
        if(redisObj!=null){
            return new R().setCode(200).setData(redisObj).setMsg("OK");
        }
        synchronized (this){
            redisObj = valueOperations.get(String.valueOf(key));
            if(redisObj!=null){
                return new R().setCode(200).setData(redisObj).setMsg("OK");
            }
            T load = cacheLoadble.load();
            valueOperations.set(String.valueOf(key), load,expire, unit);  //加入缓存
            return new R().setCode(200).setData(load).setMsg("OK");
        }
    }


    public R redisFindCache(String key, long expire, TimeUnit unit, CacheLoadble<T> cacheLoadble,boolean b){
        if (!bloomFilter.isExist(key)){
            return new R().setCode(600).setData(new NullValueResultDO()).setMsg("非法访问");
        }
        //查询缓存
        Object redisObj = valueOperations.get(String.valueOf(key));
        //命中缓存
        if(redisObj != null) {
            //正常返回数据
            return new R().setCode(200).setData(redisObj).setMsg("OK");
        }
        T load = cacheLoadble.load();//查询数据库
        if (load != null) {
            valueOperations.set(key, load,expire, unit);  //加入缓存
            return new R().setCode(200).setData(load).setMsg("OK");
        }
        return new R().setCode(500).setData(new NullValueResultDO()).setMsg("查询无果");
    }
}
