package com.xufree.learning.redis.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import redis.clients.jedis.JedisClusterInfoCache;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
public class ConfigBean {
    @Bean
    public RestTemplate getRestTemplate()
    {
        return new RestTemplate();
    }



    @Bean
    public JedisClusterInfoCache jedisClusterInfoCache()
    {
        // Jedis连接池配置
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        // 最大空闲连接数, 默认8个
        jedisPoolConfig.setMaxIdle(200);
        // 最大连接数, 默认8个
        jedisPoolConfig.setMaxTotal(1000);
        //最小空闲连接数, 默认0
        jedisPoolConfig.setMinIdle(100);
        // 获取连接时的最大等待毫秒数(如果设置为阻塞时BlockWhenExhausted),如果超时就抛异常, 小于零:阻塞不确定的时间,  默认-1
        jedisPoolConfig.setMaxWaitMillis(3000); // 设置2秒
        //对拿到的connection进行validateObject校验
        jedisPoolConfig.setTestOnBorrow(false);
        return new JedisClusterInfoCache(jedisPoolConfig,2000);
    }
}
