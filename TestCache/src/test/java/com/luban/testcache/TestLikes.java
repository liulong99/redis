package com.luban.testcache;

import com.luban.testcache.entity.Talk;
import com.luban.testcache.service.LikeService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestLikes {


	@Autowired
	LikeService likeService;

	@Test
	public void contextLoads() {
		Talk talk=new Talk();
		talk.setId("1002");
		talk.setLikeUserId(555);
		talk.setStatus(true);
		boolean b = likeService.likeAndCancelLike(talk);
		System.out.println(b);

		Talk talk2=new Talk();
		talk2.setId("1002");
		talk2.setLikeUserId(333);
		boolean like = likeService.isLike(talk2);
		System.out.println(like);

		Talk talk3=new Talk();
		talk3.setId("1002");
		long likeCount = likeService.getLikeCount(talk3);
		System.out.println(likeCount);
	}

}
