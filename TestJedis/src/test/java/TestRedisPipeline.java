import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;

import java.util.List;

public class TestRedisPipeline {
    public static void main(String[] args) {
//        Jedis jedis=new Jedis("192.168.0.104",6379);
//        long start = System.currentTimeMillis();
//        for (int i=0;i<10000;i++){
//            jedis.hset("aaaa"+i,"aaaa"+i,"aaaa"+i);
//        }
//        long end = System.currentTimeMillis();
//        System.out.println(end-start);
//        jedis.close();

        Jedis jedis=new Jedis("192.168.204.209",6379);
        long start = System.currentTimeMillis();
        for (int i=0;i<100;i++){
            Pipeline pipeline=jedis.pipelined();
            for (int j=i*100;j<(i+1)*100;j++){
                pipeline.set("bbbb"+j,"bbbb"+j);
            }
            pipeline.syncAndReturnAll();
        }
        long end = System.currentTimeMillis();
        System.out.println(end-start);
        jedis.close();
    }
}
