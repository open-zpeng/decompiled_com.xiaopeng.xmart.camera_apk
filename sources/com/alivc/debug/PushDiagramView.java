package com.alivc.debug;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.alivc.videochat.resource.R;
import com.xiaopeng.libconfig.ipc.AccountConfig;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes.dex */
public class PushDiagramView extends LinearLayout {
    private static final int DATA_SIZE = 240;
    private boolean isStop;
    private List<LineChartData> mABit;
    private List<LineChartData> mAFps;
    private List<LineChartData> mAPtsData;
    private LineChartView mAudioBit;
    private LineChartView mAudioFps;
    private TextView mClear;
    private TextView mClose;
    private LineChartView mPts;
    private TextView mStop;
    private List<LineChartData> mVBit;
    private List<LineChartData> mVFps;
    private List<LineChartData> mVPtsData;
    private LineChartView mVideoBit;
    private LineChartView mVideoFps;

    public PushDiagramView(final Context context) {
        super(context);
        this.mVPtsData = new ArrayList();
        this.mAPtsData = new ArrayList();
        this.mVFps = new ArrayList();
        this.mAFps = new ArrayList();
        this.mVBit = new ArrayList();
        this.mABit = new ArrayList();
        this.isStop = false;
        LayoutInflater.from(context).inflate(R.layout.push_chart_log, this);
        View findViewById = findViewById(R.id.push_chart_log);
        LineChartView lineChartView = (LineChartView) findViewById.findViewById(R.id.pts);
        this.mPts = lineChartView;
        lineChartView.setSize(1000000.0f);
        this.mVideoFps = (LineChartView) findViewById.findViewById(R.id.video_fps);
        this.mAudioFps = (LineChartView) findViewById.findViewById(R.id.audio_fps);
        LineChartView lineChartView2 = (LineChartView) findViewById.findViewById(R.id.video_bitrate);
        this.mVideoBit = lineChartView2;
        lineChartView2.setSize(3000.0f);
        LineChartView lineChartView3 = (LineChartView) findViewById.findViewById(R.id.audio_bitrate);
        this.mAudioBit = lineChartView3;
        lineChartView3.setSize(200.0f);
        this.mPts.setMdata(this.mVPtsData, this.mAPtsData);
        this.mVideoFps.setMdata(this.mVFps);
        this.mAudioFps.setMdata(this.mAFps);
        this.mVideoBit.setMdata(this.mVBit);
        this.mAudioBit.setMdata(this.mABit);
        this.mClose = (TextView) findViewById.findViewById(R.id.close);
        this.mClear = (TextView) findViewById.findViewById(R.id.clear);
        TextView textView = (TextView) findViewById.findViewById(R.id.stop);
        this.mStop = textView;
        textView.setSelected(false);
        this.mClose.setOnClickListener(new View.OnClickListener() { // from class: com.alivc.debug.PushDiagramView.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                DebugViewManager.removePushDiagramWindow(context);
                DebugViewManager.createBigWindow(context);
            }
        });
        this.mClear.setOnClickListener(new View.OnClickListener() { // from class: com.alivc.debug.PushDiagramView.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                PushDiagramView.this.clearData();
            }
        });
        this.mStop.setOnClickListener(new View.OnClickListener() { // from class: com.alivc.debug.PushDiagramView.3
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                PushDiagramView.this.mStop.setText(PushDiagramView.this.mStop.isSelected() ? "stop" : AccountConfig.FaceIDRegisterAction.STATUS_START);
                PushDiagramView pushDiagramView = PushDiagramView.this;
                pushDiagramView.isStop = !pushDiagramView.mStop.isSelected();
                PushDiagramView.this.mStop.setSelected(PushDiagramView.this.isStop);
            }
        });
    }

    public void updateData(AlivcLivePushDebugInfo alivcLivePushDebugInfo, String str) {
        if (this.isStop) {
            return;
        }
        LineChartView lineChartView = this.mPts;
        if (lineChartView != null) {
            if (lineChartView.getSize() < ((float) alivcLivePushDebugInfo.getCurrentlyUploadedVideoFramePts())) {
                LineChartView lineChartView2 = this.mPts;
                lineChartView2.setSize(lineChartView2.getSize() * 100.0f);
            }
            this.mVPtsData.add(new LineChartData(str, alivcLivePushDebugInfo.getCurrentlyUploadedVideoFramePts()));
            this.mAPtsData.add(new LineChartData(str, alivcLivePushDebugInfo.getCurrentlyUploadedAudioFramePts()));
            if (this.mVPtsData.size() > 240) {
                this.mVPtsData.remove(0);
            }
            if (this.mAPtsData.size() > 240) {
                this.mAPtsData.remove(0);
            }
            this.mPts.setMdata(this.mVPtsData, this.mAPtsData);
        }
        if (this.mVideoFps != null) {
            this.mVFps.add(new LineChartData(str, alivcLivePushDebugInfo.getVideoEncodeFps()));
            if (this.mVFps.size() > 240) {
                this.mVFps.remove(0);
            }
            this.mVideoFps.setMdata(this.mVFps);
        }
        if (this.mAudioFps != null) {
            this.mAFps.add(new LineChartData(str, alivcLivePushDebugInfo.getAudioEncodeFps()));
            if (this.mAFps.size() > 240) {
                this.mAFps.remove(0);
            }
            this.mAudioFps.setMdata(this.mAFps);
        }
        if (this.mVideoBit != null) {
            this.mVBit.add(new LineChartData(str, alivcLivePushDebugInfo.getVideoUploadBitrate()));
            if (this.mVBit.size() > 240) {
                this.mVBit.remove(0);
            }
            this.mVideoBit.setMdata(this.mVBit);
        }
        if (this.mAudioBit != null) {
            this.mABit.add(new LineChartData(str, alivcLivePushDebugInfo.getAudioUploadBitrate()));
            if (this.mABit.size() > 240) {
                this.mABit.remove(0);
            }
            this.mAudioBit.setMdata(this.mABit);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void clearData() {
        this.mVPtsData.clear();
        this.mAPtsData.clear();
        this.mVFps.clear();
        this.mAFps.clear();
        this.mVBit.clear();
        this.mABit.clear();
    }
}
