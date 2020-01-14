package com.luban;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.concurrent.locks.ReentrantLock;
 
/**
 * Redis 工具类
 * @author caspar
 *
 */
public class RedisUtil {

//	protected static ReentrantLock lockPool = new ReentrantLock();
//	protected static ReentrantLock lockJedis = new ReentrantLock();
//
//	private static JedisPool jedisPool = null;
//
//	/**
//	 * redis过期时间,以秒为单位
//	 */
//	public final static int EXRP_HOUR = 60 * 60;            //一小时
//	public final static int EXRP_DAY = 60 * 60 * 24;        //一天
//	public final static int EXRP_MONTH = 60 * 60 * 24 * 30;    //一个月
//
//	/**
//	 * 初始化Redis连接池
//	 */
//	private static void initialPool() {
//		try {
//			JedisPoolConfig config = new JedisPoolConfig();
//			config.setMaxTotal(100);
//			config.setMaxIdle(1000);
//			config.setMinIdle(1);
//			config.setMaxWaitMillis(2000);
//			config.setTestOnBorrow(true);
//			config.setTestOnReturn(true);
//			jedisPool = new JedisPool(config, "192.168.0.104", 6379, 30);
//		} catch (Exception e) {
//		}
//	}
//
//
//	/**
//	 * 在多线程环境同步初始化
//	 */
//	private static void poolInit() {
//		//断言 ，当前锁是否已经锁住，如果锁住了，就啥也不干，没锁的话就执行下面步骤
//		assert !lockPool.isHeldByCurrentThread();
//		lockPool.lock();
//		try {
//			if (jedisPool == null) {
//				initialPool();
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			lockPool.unlock();
//		}
//	}
//
//
//	public static Jedis getJedis() {
//		//断言 ，当前锁是否已经锁住，如果锁住了，就啥也不干，没锁的话就执行下面步骤
//		assert !lockJedis.isHeldByCurrentThread();
//		lockJedis.lock();
//
//		if (jedisPool == null) {
//			poolInit();
//		}
//		Jedis jedis = null;
//		try {
//			if (jedisPool != null) {
//				jedis = jedisPool.getResource();
//			}
//		} catch (Exception e) {
//		} finally {
//			returnResource(jedis);
//			lockJedis.unlock();
//		}
//		return jedis;
//	}
//
//	/**
//	 * 释放jedis资源
//	 *
//	 * @param jedis
//	 */
//	public static void returnResource(final Jedis jedis) {
//		if (jedis != null && jedisPool != null) {
//			jedisPool.returnResource(jedis);
//		}
//	}
}
	


	

