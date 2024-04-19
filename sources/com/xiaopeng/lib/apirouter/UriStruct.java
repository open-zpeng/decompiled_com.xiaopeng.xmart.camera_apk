package com.xiaopeng.lib.apirouter;
/* loaded from: classes.dex */
public class UriStruct {
    public String applicationId;
    public String processTag;
    public String serviceName;

    public String toString() {
        return this.applicationId + "." + this.serviceName;
    }
}
