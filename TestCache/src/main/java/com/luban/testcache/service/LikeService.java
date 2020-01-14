package com.luban.testcache.service;

import com.luban.testcache.entity.Talk;

public interface LikeService {

    boolean likeAndCancelLike(Talk talk);


    long getLikeCount(Talk talk);


    boolean isLike(Talk talk);

}
