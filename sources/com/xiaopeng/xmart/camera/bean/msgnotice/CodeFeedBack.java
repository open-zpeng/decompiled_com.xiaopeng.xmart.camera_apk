package com.xiaopeng.xmart.camera.bean.msgnotice;

import com.google.gson.annotations.SerializedName;
/* loaded from: classes.dex */
public class CodeFeedBack {
    @SerializedName("code")
    private int code;

    public CodeFeedBack(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String toString() {
        return "CodeFeedBack{code=" + this.code + '}';
    }
}
