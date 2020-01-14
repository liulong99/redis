import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.ScanParams;
import redis.clients.jedis.ScanResult;
import redis.clients.jedis.params.SetParams;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestJedis {
    //只修改了bind 为0.0.0.0  开启了保护模式  设置外网访问 成功
    // 注释掉了bind   设置外网访问失败
    //注释掉了bind  关闭了保护模式
    public static void main(String[] args) {
        Jedis jedis=new Jedis("192.168.204.188",6379);

        System.out.println(jedis.ping());
        jedis.close();
    }

    private Jedis jedis;

    @Before
    public void initJedis(){
        jedis=new Jedis("47.94.158.155",6379);
    }


    @Test
    public void testKey(){
        System.out.println(jedis.keys("*"));
        System.out.println(jedis.exists("k1"));
        jedis.expire("k1", 60);
        System.out.println(jedis.ttl("k1"));
        System.out.println(jedis.type("k1"));

        System.out.println(jedis.incr("count"));
        System.out.println(jedis.incr("count"));
        System.out.println(jedis.incr("count"));
        System.out.println(jedis.decr("count"));
        System.out.println(jedis.decr("count"));
        System.out.println(jedis.decr("count"));

        System.out.println(jedis.incrBy("count",2));
        System.out.println(jedis.incrBy("count",2));
        System.out.println(jedis.decrBy("count",2));
        System.out.println(jedis.decrBy("count",2));
    }

    @Test
    public void testString(){
        System.out.println(jedis.set("k1", "v2"));
        System.out.println(jedis.get("k1"));
        System.out.println(jedis.del("k1"));

        System.out.println(jedis.append("k1","v1"));
        System.out.println(jedis.append("k1","v1"));
        System.out.println(jedis.get("k1"));
        System.out.println(jedis.strlen("k1"));
        System.out.println(jedis.mset("k1", "v1", "k2", "v2", "k3", "v3"));
        System.out.println(jedis.mget("k1",  "k2", "k3"));


        System.out.println(jedis.setnx("k1","kkkkk"));
        System.out.println(jedis.get("k1"));

        System.out.println(jedis.setex("k9", 60, "v9"));
        System.out.println(jedis.ttl("k9"));

        System.out.println("=========================");
        SetParams setParams=new SetParams();
        setParams.ex(100); //设置过期时间
        setParams.nx(); //如果点前key已经存在  则不设置值
//        其实NX表示使用NX模式，PX表示过期时间的单位，这里表示毫秒
//        System.out.println(jedis.set("k11", "k11", "NX","PX",100000));
        System.out.println(jedis.set("k11", "k11", setParams));
        System.out.println(jedis.ttl("k11"));
    }


    @Test
    public void testList(){
//        jedis.del("lpush");
//        jedis.del("rpush");
        System.out.println(jedis.lpush("lpush","v1","v2","v3","v4"));
        System.out.println(jedis.rpush("rpush","v1","v2","v3","v4"));
        System.out.println(jedis.lrange("lpush",0,-1));
        System.out.println(jedis.lrange("rpush",0,-1));

        System.out.println(jedis.rpop("lpush"));
        System.out.println(jedis.rpop("rpush"));

        System.out.println(jedis.lpop("lpush"));
        System.out.println(jedis.lpop("rpush"));
    }


    @Test
    public void testSet(){
        jedis.sadd("set1","v1","v2","v3","v4","v5","v6","v7");
        System.out.println(jedis.smembers("set1"));
        System.out.println(jedis.sismember("set1","v1"));

        jedis.sadd("set2","v1","v2","v3","v4","v11","v12");
        System.out.println(jedis.sdiff("set1", "set2")); //在第一个set里面而不在后面任何一个set里面的项(差集)
        System.out.println(jedis.sinter("set1", "set2"));//在第一个set和第二个set中都有的 （交集）
        System.out.println(jedis.sunion ("set1", "set2"));//两个集合所有元素（并集）
    }


    @Test
    public void testHash(){
        Map<String,String> map= new HashMap<>();
        map.put("k1","v1");
        map.put("k2","v2");
        map.put("k3","v3");
        System.out.println(jedis.hset("hash", map));

        System.out.println(jedis.hkeys("hash"));
        System.out.println(jedis.hvals("hash"));
        System.out.println(jedis.hgetAll("hash"));
    }


    @Test
    public void testZset(){
        Map<String,Double> map=new HashMap();
        map.put("k1",10d);
        map.put("k4",40d);
        map.put("k3",30d);
        map.put("k2",20d);
        System.out.println(jedis.zadd("zset", map));
        System.out.println(jedis.zrange("zset", 0, -1));
        System.out.println(jedis.zrangeWithScores("zset", 0, -1));
    }



    @Test
    public void testScan(){
        this.methodScan("0");
    }

    public void methodScan(String cursor){
        // 游标初始值为0
        String key = "k*";
        ScanParams scanParams = new ScanParams();
        scanParams.match(key);
        scanParams.count(3);
        ScanResult<String> scanResult = jedis.scan(cursor, scanParams);
        cursor = scanResult.getCursor();// 返回0 说明遍历完成
        System.out.println("返回游标为："+cursor);
        List<String> list = scanResult.getResult();
        for(int m = 0;m < list.size();m++){
            String mapentry = list.get(m);
            System.out.println("查询出key:"+mapentry);
        }
        System.out.println("==========================");
        if(!"0".equals(cursor)){
            this.methodScan(cursor);
        }
    }

    @After
    public void destroy(){
        if(jedis!=null){
            jedis.close();
        }
    }




}
