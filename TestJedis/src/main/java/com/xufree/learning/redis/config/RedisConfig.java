package com.xufree.learning.redis.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * maxtotal 怎么设置
 * 1.MaxIdle接近MaxTotal
 * 2.希望业务并发量
 * 3.客户端执行时间
 * 4.应用个数*最大连接数不能超过服务器端的最大连接数   redis默认最大连接数为10000
 * 5.资源开销：例如虽然希望控制空闲连接，但是不希望因为连接池的频繁释放创建连接造成不必靠开销。
 * 例如：一次命令的时间约耗时1毫秒，一个连接的qps大约是1000，业务期望的qps是50000
 * 理论的maxtotal=50000/1000=50 ，可以适当伸缩
 *
 */
@ComponentScan("com.xufree.learning.redis")
@Configuration
public class RedisConfig {
    @Bean
    public JedisPool jedisPool(){
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(150);  //最大连接数
        jedisPoolConfig.setMaxIdle(150); //最大空闲连接数
        jedisPoolConfig.setMinIdle(50);   //最小空闲连接数
        jedisPoolConfig.setMaxWaitMillis(2000); //获取连接时最大等待时间
        jedisPoolConfig.setTestOnBorrow(true); //获取连接时检查是否可用
        jedisPoolConfig.setTestOnReturn(true); //返回连接时检查是否可用
        jedisPoolConfig.setTestWhileIdle(true);  //是否开启空闲资源监测
        jedisPoolConfig.setTimeBetweenEvictionRunsMillis(300000); //-1不检测   单位为毫秒  空闲资源监测周期
        jedisPoolConfig.setMinEvictableIdleTimeMillis(30*60*1000);//资源池中资源最小空闲时间 单位为毫秒  达到此值后空闲资源将被移除
        jedisPoolConfig.setNumTestsPerEvictionRun(30); //做空闲监测时，每次采集的样本数  -1代表对所有连接做监测
        JedisPool jedisPool = new JedisPool(jedisPoolConfig,"192.168.0.104",6379);
        return jedisPool;
    }
}
