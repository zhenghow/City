package com.example.edz.myapplication.bean;

/**
 * Created by EDZ on 2018/4/4.
 *
 * 版本更新功能，版本号基类
 *
 */

public class VersionBean {


    /**
     * code : 10000
     * msg : 成功！
     * object : {"versionMin":"1.0.3","versionMax":"1.0.3","updateUrl":null,"content":""}
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
         * versionMin : 1.0.3
         * versionMax : 1.0.3
         * updateUrl : null
         * content :
         */

        private String versionMin;
        private String versionMax;
        private String updateUrl;
        private String content;

        public String getVersionMin() {
            return versionMin;
        }

        public void setVersionMin(String versionMin) {
            this.versionMin = versionMin;
        }

        public String getVersionMax() {
            return versionMax;
        }

        public void setVersionMax(String versionMax) {
            this.versionMax = versionMax;
        }

        public void setUpdateUrl(String updateUrl) {
            this.updateUrl = updateUrl;
        }

        public String getUpdateUrl() {

            return updateUrl;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}
