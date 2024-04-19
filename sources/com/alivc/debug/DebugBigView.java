package com.alivc.debug;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.alivc.videochat.resource.R;
import org.eclipse.paho.client.mqttv3.MqttTopic;
/* loaded from: classes.dex */
public class DebugBigView extends LinearLayout implements View.OnClickListener {
    public static int viewHeight;
    public static int viewWidth;
    private float cpuValue;
    private TextView mCpuChange;
    private TextView mCurrentCpu;
    private TextView mCurrentMem;
    private TextView mDataList;
    private TextView mDiagramList;
    private TextView mLogCheck;
    private TextView mMemChange;
    private WindowManager.LayoutParams mParams;
    private float memValue;
    private WindowManager windowManager;
    private float xDownInScreen;
    private float xInScreen;
    private float xInView;
    private float yDownInScreen;
    private float yInScreen;
    private float yInView;

    public DebugBigView(final Context context) {
        super(context);
        this.cpuValue = 0.0f;
        this.memValue = 0.0f;
        LayoutInflater.from(context).inflate(R.layout.debug_view_big, this);
        View findViewById = findViewById(R.id.big_window_layout);
        this.windowManager = (WindowManager) context.getApplicationContext().getSystemService("window");
        viewWidth = findViewById.getLayoutParams().width;
        viewHeight = findViewById.getLayoutParams().height;
        this.mLogCheck = (TextView) findViewById(R.id.log_check);
        this.mDataList = (TextView) findViewById(R.id.data_list);
        this.mDiagramList = (TextView) findViewById(R.id.diagram_list);
        this.mCurrentCpu = (TextView) findViewById(R.id.current_cpu);
        this.mCpuChange = (TextView) findViewById(R.id.cpu_change);
        this.mCurrentMem = (TextView) findViewById(R.id.current_mem);
        this.mMemChange = (TextView) findViewById(R.id.mem_change);
        this.mLogCheck.setOnClickListener(this);
        this.mDataList.setOnClickListener(this);
        this.mDiagramList.setOnClickListener(this);
        ((Button) findViewById(R.id.close)).setOnClickListener(new View.OnClickListener() { // from class: com.alivc.debug.DebugBigView.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                DebugViewManager.removeBigWindow(context);
                DebugViewManager.removeSmallWindow(context);
            }
        });
        ((TextView) findViewById(R.id.back)).setOnClickListener(new View.OnClickListener() { // from class: com.alivc.debug.DebugBigView.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                DebugViewManager.removeBigWindow(context);
                DebugViewManager.createSmallWindow(context);
            }
        });
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        int action = motionEvent.getAction();
        if (action != 0) {
            if (action != 2) {
                return true;
            }
            this.xInScreen = motionEvent.getRawX();
            this.yInScreen = motionEvent.getRawY();
            updateViewPosition();
            return true;
        }
        this.xInView = motionEvent.getX();
        this.yInView = motionEvent.getY();
        this.xDownInScreen = motionEvent.getRawX();
        this.yDownInScreen = motionEvent.getRawY();
        this.xInScreen = motionEvent.getRawX();
        this.yInScreen = motionEvent.getRawY();
        return true;
    }

    private void updateViewPosition() {
        this.mParams.x = (int) (this.xInScreen - this.xInView);
        this.mParams.y = (int) (this.yInScreen - this.yInView);
        this.windowManager.updateViewLayout(this, this.mParams);
    }

    public void setParams(WindowManager.LayoutParams layoutParams) {
        this.mParams = layoutParams;
    }

    public void updateInfo(float f, float f2) {
        this.mCurrentMem.setText(String.format("%.2f", Float.valueOf(f2)));
        float f3 = this.memValue;
        if (f2 - f3 >= 0.0f) {
            this.mMemChange.setText(MqttTopic.SINGLE_LEVEL_WILDCARD + String.format("%.2f", Float.valueOf(f2 - this.memValue)));
            this.mMemChange.setTextColor(getResources().getColor(R.color.light_red));
        } else {
            this.mMemChange.setText(String.format("%.2f", Float.valueOf(f2 - f3)));
            this.mMemChange.setTextColor(getResources().getColor(R.color.light_green));
        }
        this.mCurrentCpu.setText(String.format("%.2f", Float.valueOf(f)));
        float f4 = this.cpuValue;
        if (f - f4 >= 0.0f) {
            this.mCpuChange.setText(MqttTopic.SINGLE_LEVEL_WILDCARD + String.format("%.2f", Float.valueOf(f - this.cpuValue)));
            this.mCpuChange.setTextColor(getResources().getColor(R.color.light_red));
        } else {
            this.mCpuChange.setText(String.format("%.2f", Float.valueOf(f - f4)));
            this.mCpuChange.setTextColor(getResources().getColor(R.color.light_green));
        }
        this.memValue = f2;
        this.cpuValue = f;
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.log_check) {
            DebugViewManager.removeBigWindow(getContext().getApplicationContext());
            DebugViewManager.createPushLogWindow(getContext().getApplicationContext());
        } else if (id == R.id.data_list) {
            DebugViewManager.removeBigWindow(getContext().getApplicationContext());
            DebugViewManager.createPushDataWindow(getContext().getApplicationContext());
        } else if (id == R.id.diagram_list) {
            DebugViewManager.removeBigWindow(getContext().getApplicationContext());
            DebugViewManager.createPushDiagramView(getContext().getApplicationContext());
        }
    }
}
