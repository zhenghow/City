package com.example.edz.myapplication.bean;

/**
 * Created by EDZ on 2018/4/10.
 */

public class TimeBean {

    /**
     * code : 10120
     * msg : 验证码时间错误！
     * object : 58126
     */

    private int code;
    private String msg;
    private long object;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setObject(long object) {
        this.object = object;
    }

    public long getObject() {

        return object;
    }
}
