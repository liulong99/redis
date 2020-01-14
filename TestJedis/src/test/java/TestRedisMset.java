import com.xufree.learning.redis.redis.MyRedisCluster;
import redis.clients.jedis.*;

import java.util.*;

public class TestRedisMset {
    public static void main(String[] args) {
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
        MyRedisCluster myRedisCluster=new MyRedisCluster(nodesList,2000,2000,5,"123456",jedisPoolConfig);


        myRedisCluster.mset("a","a","b","b","c","c");
//        JedisCluster jedisCluster=new JedisCluster(nodesList,2000,2000,5,"123456",jedisPoolConfig);
//        System.out.println(jedisCluster.mset("{user}:taibaikey", "taibaivalue", "{user}:taibaikey111", "taibaivalue111"));
    }

    public static Map<Integer,Map<String,ScanParams>> temp(Map<Integer, ScanResult> integerScanResultMap,ScanParams scanParams){
        Set<Integer> integers = integerScanResultMap.keySet();
        Map<Integer,Map<String,ScanParams>> mapMap=new HashMap<>();
        for (Integer integer : integers) {
            Map<String,ScanParams> map=new HashMap<>();
            ScanResult scanResult = integerScanResultMap.get(integer);
            List result = scanResult.getResult();
            String cursor = scanResult.getCursor();
            map.put(cursor,scanParams);
            mapMap.put(integer,map);
            for (Object o : result) {
                System.out.println(o);
            }
            System.out.println("===========");
        }
        return mapMap;
    }
}
