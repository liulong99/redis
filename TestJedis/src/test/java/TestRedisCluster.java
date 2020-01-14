import com.google.common.collect.Lists;
import com.xufree.learning.redis.redis.MyRedisCluster;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.*;
import redis.clients.jedis.util.JedisClusterCRC16;

import java.util.*;

public class TestRedisCluster {

    public static void main(String[] args) {
        Logger logger= LoggerFactory.getLogger(TestRedisCluster.class);
        Set<HostAndPort> nodesList=new HashSet<>();
        nodesList.add(new HostAndPort("192.168.204.188",7000));
        nodesList.add(new HostAndPort("192.168.204.188",7001));
        nodesList.add(new HostAndPort("192.168.204.188",7002));
        nodesList.add(new HostAndPort("192.168.204.188",7003));
        nodesList.add(new HostAndPort("192.168.204.188",7004));
        nodesList.add(new HostAndPort("192.168.204.188",7005));


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
        MyRedisCluster redisCluster=new MyRedisCluster(nodesList,2000,2000,5,"123456",jedisPoolConfig);

//        JedisCluster jedisCluster=new JedisCluster(nodesList,2000,2000,5,"123456",jedisPoolConfig);
        System.out.println(redisCluster.mset("k1", "v1", "k2", "v2", "k3", "v3"));
//        System.out.println(redisCluster.mget("k1","k2", "k3" ));
//        while (true) {
//            try {
//                String s = UUID.randomUUID().toString();
//                jedisCluster.set("k" + s, "v" + s);
//                System.out.println(jedisCluster.get("k" + s));
//                Thread.sleep(1000);
//            }catch (Exception e) {
//                logger.error(e.getMessage());
//            }
//        }
    }
}
