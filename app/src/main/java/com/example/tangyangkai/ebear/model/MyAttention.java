package com.example.tangyangkai.ebear.model;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2016/3/11.
 */
public class MyAttention extends BmobObject {

    private String userId;
    private String atentionId;

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setAtentionId(String atentionId) {
        this.atentionId = atentionId;
    }

    public String getAtentionId() {
        return atentionId;
    }
}
