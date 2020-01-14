package com.luban;

import org.redisson.Redisson;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
@ComponentScan("com.luban")
public class SpringConfig {

//    @Scope("prototype")
//    @Lazy
//    @Bean
//    public Jedis jedis(JedisPool jedisPool){
//        return jedisPool.getResource();
//    }

    @Bean
    public JedisPool jedisPool(){
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(101);
        jedisPoolConfig.setMaxIdle(1000);
        jedisPoolConfig.setMinIdle(1);
        jedisPoolConfig.setMaxWaitMillis(2000);
        jedisPoolConfig.setTestOnBorrow(true);
        jedisPoolConfig.setTestOnReturn(true);
        JedisPool jedisPool = new JedisPool(jedisPoolConfig,"192.168.204.188",6379);
        return jedisPool;
    }

    @Bean
    public Redisson redisson(){
        Config config=new Config();
//        config.useSingleServer().setAddress("redis://192.168.204.188:6379").setDatabase(0);
        config.useClusterServers().addNodeAddress("redis://192.168.204.188:7000").addNodeAddress("redis://192.168.204.188:7001")
                .addNodeAddress("redis://192.168.204.188:7002").addNodeAddress("redis://192.168.204.188:7003")
                .addNodeAddress("redis://192.168.204.188:7004").addNodeAddress("redis://192.168.204.188:7005");
        config.useClusterServers().setPassword("123456");
        Redisson redisson = (Redisson) Redisson.create(config);
        return redisson;
    }


//    @Bean
//    public Redisson redisson6380(){
//        Config config=new Config();
//        config.useSingleServer().setAddress("redis://192.168.204.188:6380").setDatabase(0);
//        Redisson redisson = (Redisson) Redisson.create(config);
//        return redisson;
//    }


//    @Bean
//    public Redisson redisson6381(){
//        Config config=new Config();
//        config.useSingleServer().setAddress("redis://192.168.204.188:6381").setDatabase(0);
//        Redisson redisson = (Redisson) Redisson.create(config);
//        return redisson;
//    }


}
