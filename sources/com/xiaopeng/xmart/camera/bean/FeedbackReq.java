package com.xiaopeng.xmart.camera.bean;
/* loaded from: classes.dex */
public class FeedbackReq<T> {
    private T msg_content;
    private String msg_id;
    private String msg_ref;
    private int msg_type;
    private int service_type;

    public String getMsg_id() {
        return this.msg_id;
    }

    public void setMsg_id(String msg_id) {
        this.msg_id = msg_id;
    }

    public void setMsg_ref(String msg_ref) {
        this.msg_ref = msg_ref;
    }

    public int getMsg_type() {
        return this.msg_type;
    }

    public void setMsg_type(int msg_type) {
        this.msg_type = msg_type;
    }

    public int getService_type() {
        return this.service_type;
    }

    public void setService_type(int service_type) {
        this.service_type = service_type;
    }

    public T getMsg_content() {
        return this.msg_content;
    }

    public void setMsg_content(T msg_content) {
        this.msg_content = msg_content;
    }
}
