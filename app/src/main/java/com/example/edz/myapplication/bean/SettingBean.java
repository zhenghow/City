package com.example.edz.myapplication.bean;

/**
 * Created by EDZ on 2018/4/3.
 */

public class SettingBean {

    /**
     * code : 10000
     * msg : 成功！
     * object : {"idcard":"1305351985101410000","mobile":"17725310000","name":"赵亦磊10000","telephone":"0322-58711111"}
     */

    private int code;
    private String msg;
    private ObjectBean object;

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

    public ObjectBean getObject() {
        return object;
    }

    public void setObject(ObjectBean object) {
        this.object = object;
    }

    public static class ObjectBean {
        /**
         * idcard : 1305351985101410000
         * mobile : 17725310000
         * name : 赵亦磊10000
         * telephone : 0322-58711111
         */

        private String idcard;
        private String mobile;
        private String name;
        private String telephone;

        public String getIdcard() {
            return idcard;
        }

        public void setIdcard(String idcard) {
            this.idcard = idcard;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getTelephone() {
            return telephone;
        }

        public void setTelephone(String telephone) {
            this.telephone = telephone;
        }
    }
}
