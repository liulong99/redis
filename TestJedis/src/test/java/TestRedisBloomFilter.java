import com.xufree.learning.redis.config.RedisConfig;
import com.xufree.learning.redis.filter.RedisBloomFilter;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class TestRedisBloomFilter {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext annotationConfigApplicationContext=new AnnotationConfigApplicationContext(RedisConfig.class);
        RedisBloomFilter bean = annotationConfigApplicationContext.getBean(RedisBloomFilter.class);
        for (int i=0;i<1000;i++){
            bean.put("taibai","haha"+i);
        }
        System.out.println(bean.isExist("taibai", "haha3"));
        System.out.println(bean.isExist("taibai", "haha6"));
        System.out.println(bean.isExist("taibai", "haha100000"));
        annotationConfigApplicationContext.close();
    }
}
