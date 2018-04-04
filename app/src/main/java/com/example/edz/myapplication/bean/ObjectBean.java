package com.example.edz.myapplication.bean;

/**
 * Created by EDZ on 2018/3/28.
 */

public class ObjectBean {

    /**
     * id : 5
     * mobile : 17725355607
     * smsCode : ABCDEF
     * smsCodeTime : 1522135069448
     * inviteCode : ABCD
     * inviteCodeCnt : 0
     * nickname : zyl
     * name : null
     * idcard : null
     * idcardTime : 0
     * coinCity : 0
     * coinAtm : 0
     * token : f07d4531-bddf-47df-8ff9-e09c30c7a9ba
     */

    private int id;
    private String mobile;
    private String smsCode;
    private long smsCodeTime;
    private String inviteCode;
    private int inviteCodeCnt;
    private String nickname;
    private Object name;
    private Object idcard;
    private int idcardTime;
    private int coinCity;
    private int coinAtm;
    private String token;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getSmsCode() {
        return smsCode;
    }

    public void setSmsCode(String smsCode) {
        this.smsCode = smsCode;
    }

    public long getSmsCodeTime() {
        return smsCodeTime;
    }

    public void setSmsCodeTime(long smsCodeTime) {
        this.smsCodeTime = smsCodeTime;
    }

    public String getInviteCode() {
        return inviteCode;
    }

    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
    }

    public int getInviteCodeCnt() {
        return inviteCodeCnt;
    }

    public void setInviteCodeCnt(int inviteCodeCnt) {
        this.inviteCodeCnt = inviteCodeCnt;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Object getName() {
        return name;
    }

    public void setName(Object name) {
        this.name = name;
    }

    public Object getIdcard() {
        return idcard;
    }

    public void setIdcard(Object idcard) {
        this.idcard = idcard;
    }

    public int getIdcardTime() {
        return idcardTime;
    }

    public void setIdcardTime(int idcardTime) {
        this.idcardTime = idcardTime;
    }

    public int getCoinCity() {
        return coinCity;
    }

    public void setCoinCity(int coinCity) {
        this.coinCity = coinCity;
    }

    public int getCoinAtm() {
        return coinAtm;
    }

    public void setCoinAtm(int coinAtm) {
        this.coinAtm = coinAtm;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
