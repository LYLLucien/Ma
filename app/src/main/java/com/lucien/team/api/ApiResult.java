package com.lucien.team.api;

/**
 * Created by lucien.li on 2015/6/4.
 */
public class ApiResult {

    public final static int API_STATE_NORMAL = 0;
    public final static int API_STATE_ERROR = -1;

    private int state = -1;
    private String msg = "";
    private long payload;

    public boolean isValid() {
        return state == 0;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public long getPayload() {
        return payload;
    }

    public void setPayload(long payload) {
        this.payload = payload;
    }
}

