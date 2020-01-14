import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;

import java.util.UUID;

public class TestHyperLogLog {
    public static void main(String[] args) {
        Jedis jedis=new Jedis("192.168.0.104",6379);
        for (int i=0;i<100;i++){
            Pipeline pipelined = jedis.pipelined();
            for (int j=i*100;j<(i+1)*100;j++){
                pipelined.pfadd("haha22","02f"+j);
                System.out.println(j);
            }
            pipelined.close();
        }
        System.out.println(jedis.pfcount("haha22"));
    }
}
