package com.example.edz.myapplication.bean;

import java.util.List;

/**
 * Created by EDZ on 2018/4/25.
 *
 * 友盟推送 基类
 */

public class PushInfo {

    /**
     * policy : {"expire_time":"2018-04-28 09:21:14"}
     * description : test
     * production_mode : true
     * appkey : 5ad40be7b27b0a0edb0000ad
     * payload : {"body":{"title":"test","ticker":"test","text":"test","after_open":"go_app","play_vibrate":"false","play_lights":"false","play_sound":"true"},"display_type":"notification"}
     * mipush : true
     * mi_activity : com.example.edz.myapplication.activity.PushActivity
     * filter : {"where":{"and":[{"or":[{"app_version":"1.0.1050"}]}]}}
     * type : groupcast
     * timestamp : 1524621487087
     */

    private PolicyBean policy;
    private String description;
    private boolean production_mode;
    private String appkey;
    private PayloadBean payload;
    private boolean mipush;
    private String mi_activity;
    private FilterBean filter;
    private String type;
    private String timestamp;

    public PolicyBean getPolicy() {
        return policy;
    }

    public void setPolicy(PolicyBean policy) {
        this.policy = policy;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isProduction_mode() {
        return production_mode;
    }

    public void setProduction_mode(boolean production_mode) {
        this.production_mode = production_mode;
    }

    public String getAppkey() {
        return appkey;
    }

    public void setAppkey(String appkey) {
        this.appkey = appkey;
    }

    public PayloadBean getPayload() {
        return payload;
    }

    public void setPayload(PayloadBean payload) {
        this.payload = payload;
    }

    public boolean isMipush() {
        return mipush;
    }

    public void setMipush(boolean mipush) {
        this.mipush = mipush;
    }

    public String getMi_activity() {
        return mi_activity;
    }

    public void setMi_activity(String mi_activity) {
        this.mi_activity = mi_activity;
    }

    public FilterBean getFilter() {
        return filter;
    }

    public void setFilter(FilterBean filter) {
        this.filter = filter;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public static class PolicyBean {
        /**
         * expire_time : 2018-04-28 09:21:14
         */

        private String expire_time;

        public String getExpire_time() {
            return expire_time;
        }

        public void setExpire_time(String expire_time) {
            this.expire_time = expire_time;
        }
    }

    public static class PayloadBean {
        /**
         * body : {"title":"test","ticker":"test","text":"test","after_open":"go_app","play_vibrate":"false","play_lights":"false","play_sound":"true"}
         * display_type : notification
         */

        private BodyBean body;
        private String display_type;

        public BodyBean getBody() {
            return body;
        }

        public void setBody(BodyBean body) {
            this.body = body;
        }

        public String getDisplay_type() {
            return display_type;
        }

        public void setDisplay_type(String display_type) {
            this.display_type = display_type;
        }

        public static class BodyBean {
            /**
             * title : test
             * ticker : test
             * text : test
             * after_open : go_app
             * play_vibrate : false
             * play_lights : false
             * play_sound : true
             */

            private String title;
            private String ticker;
            private String text;
            private String after_open;
            private String play_vibrate;
            private String play_lights;
            private String play_sound;

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getTicker() {
                return ticker;
            }

            public void setTicker(String ticker) {
                this.ticker = ticker;
            }

            public String getText() {
                return text;
            }

            public void setText(String text) {
                this.text = text;
            }

            public String getAfter_open() {
                return after_open;
            }

            public void setAfter_open(String after_open) {
                this.after_open = after_open;
            }

            public String getPlay_vibrate() {
                return play_vibrate;
            }

            public void setPlay_vibrate(String play_vibrate) {
                this.play_vibrate = play_vibrate;
            }

            public String getPlay_lights() {
                return play_lights;
            }

            public void setPlay_lights(String play_lights) {
                this.play_lights = play_lights;
            }

            public String getPlay_sound() {
                return play_sound;
            }

            public void setPlay_sound(String play_sound) {
                this.play_sound = play_sound;
            }
        }
    }

    public static class FilterBean {
        /**
         * where : {"and":[{"or":[{"app_version":"1.0.1050"}]}]}
         */

        private WhereBean where;

        public WhereBean getWhere() {
            return where;
        }

        public void setWhere(WhereBean where) {
            this.where = where;
        }

        public static class WhereBean {
            private List<AndBean> and;

            public List<AndBean> getAnd() {
                return and;
            }

            public void setAnd(List<AndBean> and) {
                this.and = and;
            }

            public static class AndBean {
                private List<OrBean> or;

                public List<OrBean> getOr() {
                    return or;
                }

                public void setOr(List<OrBean> or) {
                    this.or = or;
                }

                public static class OrBean {
                    /**
                     * app_version : 1.0.1050
                     */

                    private String app_version;

                    public String getApp_version() {
                        return app_version;
                    }

                    public void setApp_version(String app_version) {
                        this.app_version = app_version;
                    }
                }
            }
        }
    }
}
