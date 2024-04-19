package com.xiaopeng.xmart.camera.view.preview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.SurfaceView;
import com.xiaopeng.xmart.camera.utils.SurfaceViewReflection;
/* loaded from: classes.dex */
public class CameraSurfaceView extends SurfaceView {
    public CameraSurfaceView(Context context) {
        this(context, null);
    }

    public CameraSurfaceView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CameraSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        SurfaceViewReflection.setRoundCorner(this);
    }
}
