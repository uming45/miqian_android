package com.miqian.mq.entity;

/**
 * Created by guolei_wang on 15/9/24.
 */
public class GetRegularResult extends Meta {
    public GetRegularInfo getData() {
        return data;
    }

    public void setData(GetRegularInfo data) {
        this.data = data;
    }

    private GetRegularInfo data;
}