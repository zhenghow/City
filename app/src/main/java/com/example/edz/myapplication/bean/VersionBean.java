package com.example.edz.myapplication.bean;

/**
 * Created by EDZ on 2018/4/4.
 */

public class VersionBean {


    /**
     * code : 10000
     * msg : 成功！
     * object : {"versionMin":"0.0.0","versionMax":"1.0.0"}
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
         * versionMin : 0.0.0
         * versionMax : 1.0.0
         */

        private String versionMin;
        private String versionMax;
        private String updateUrl;
        private String serverUrl;

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

        public void setServerUrl(String serverUrl) {
            this.serverUrl = serverUrl;
        }

        public String getUpdateUrl() {

            return updateUrl;
        }

        public String getServerUrl() {
            return serverUrl;
        }
    }
}
