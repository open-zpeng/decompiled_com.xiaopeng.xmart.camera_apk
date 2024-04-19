package com.xiaopeng.xmart.livepush.bean;
/* loaded from: classes.dex */
public class LiveUrlRes {
    private int code;
    private LiveUrl data;

    /* loaded from: classes.dex */
    public class LiveUrl {
        private String live_url;
        private String push_stream_url;

        public LiveUrl() {
        }

        public String getPush_stream_url() {
            return this.push_stream_url;
        }

        public void setPush_stream_url(String push_stream_url) {
            this.push_stream_url = push_stream_url;
        }

        public String getLive_url() {
            return this.live_url;
        }

        public void setLive_url(String live_url) {
            this.live_url = live_url;
        }
    }

    public int getCode() {
        return this.code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public LiveUrl getData() {
        return this.data;
    }

    public void setData(LiveUrl data) {
        this.data = data;
    }
}
