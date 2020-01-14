package com.luban;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.Redisson;
import org.redisson.RedissonMultiLock;
import org.redisson.RedissonRedLock;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.params.SetParams;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=SpringConfig.class)
public class Dome {

    private int count=10;
//    Lock lock= new ReentrantLock();

    @Autowired
    @Qualifier("redisLock")
    private com.luban.Lock lock;




    @Autowired
    private Redisson redisson;



    @Test
    public void testRedis(){
        Jedis jedis=new Jedis("192.168.204.188",6379);
        SetParams setParams=new SetParams();
        setParams.ex(6);  //setex     设置值的同时设置过期时间
        setParams.nx();  //
        String s = UUID.randomUUID().toString();
        String lock = jedis.set("lock", s,setParams);
//        Long setnx = jedis.setnx("lock", "value2");
//        if(setnx==1){
//            jedis.expire("lock",10);
//        }

        System.out.println(lock);
    }

    @Test
    public void Test() throws InterruptedException {
        TicketsRunBle ticketsRunBle=new TicketsRunBle();
        Thread thread1=new Thread(ticketsRunBle,"窗口1");
        Thread thread2=new Thread(ticketsRunBle,"窗口2");
        Thread thread3=new Thread(ticketsRunBle,"窗口3");
        Thread thread4=new Thread(ticketsRunBle,"窗口4");
        Thread thread5=new Thread(ticketsRunBle,"窗口5");
        thread1.start();
        thread2.start();
        thread3.start();
        thread4.start();
        thread5.start();
        Thread.currentThread().join();
    }


    public class TicketsRunBle implements Runnable{

        @Override
        public void run() {
            while (count>0){
                RLock lock1 = redisson.getLock("{taibai0}:100001320055");  //100001320055
                RLock lock2 = redisson.getLock("{taibai1}:100001320055");
                RLock lock3 = redisson.getLock("{taibai2}:100001320055");
//                RLock lock1 = redisson6379.getLock("lock");
//                RLock lock2 = redisson6380.getLock("lock");
//                RLock lock3 = redisson6381.getLock("lock");
                RedissonRedLock lock = new RedissonRedLock(lock1, lock2, lock3);
                lock.lock();
                try {
                    if(count>0){
//                        Thread.sleep(ThreadLocalRandom.current().nextInt(2000, 3000));
                        System.out.println(Thread.currentThread().getName()+"售出第"+count--+"张票");
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }finally {
                    try {
                        lock.unlock();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
