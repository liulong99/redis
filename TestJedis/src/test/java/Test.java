import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.google.common.collect.Lists;
import com.xufree.learning.redis.entity.Order;
import com.xufree.learning.redis.entity.R;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

public class Test {


    private  static CountDownLatch countDownLatch = new CountDownLatch(99);

    @org.junit.Test
    public void test() throws InterruptedException {
        TicketsRunBle ticketsRunBle=new TicketsRunBle();

        for (int i = 0; i < 99; i++) {
            Thread thread1=new Thread(ticketsRunBle,"窗口"+i);
            thread1.start();
            countDownLatch.countDown();
        }
        Thread.currentThread().join();
    }
    public class TicketsRunBle implements Runnable{

        @Override
        public void run() {

            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
//            try {
//                Socket socket=new Socket("127.0.0.1",1333);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
            RestTemplate restTemplate=new RestTemplate();
            List<HttpMessageConverter<?>> fastJsonHttpMessageConverters = new ArrayList<>();
            fastJsonHttpMessageConverters.add(new FastJsonHttpMessageConverter());
            restTemplate.setMessageConverters(fastJsonHttpMessageConverters);
            R forObject = restTemplate.getForObject("http://localhost:8080/selectid?id=1003", R.class);
            System.out.println(forObject);
        }
    }
}
