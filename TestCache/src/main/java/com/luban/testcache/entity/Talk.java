package com.luban.testcache.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Talk {
    private String id;  //朋友圈id
    private Integer likeUserId;  //点赞的用户id
    private boolean status;  //状态   是点赞 还是取消点赞
}
