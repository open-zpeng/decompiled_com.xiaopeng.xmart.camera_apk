package com.alivc.debug;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.alivc.videochat.resource.R;
/* loaded from: classes.dex */
public class PushDataView extends LinearLayout {
    private TextView mClose;
    private TextView mText;

    public PushDataView(final Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.push_status_log, this);
        View findViewById = findViewById(R.id.push_status_log);
        this.mClose = (TextView) findViewById.findViewById(R.id.close);
        this.mText = (TextView) findViewById.findViewById(R.id.text);
        this.mClose.setOnClickListener(new View.OnClickListener() { // from class: com.alivc.debug.PushDataView.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                DebugViewManager.removePushDataWindow(context);
                DebugViewManager.createBigWindow(context);
            }
        });
    }

    public void updataData(AlivcLivePushDebugInfo alivcLivePushDebugInfo) {
        if (this.mText != null) {
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append(getContext().getApplicationContext().getString(R.string.debug_url)).append(alivcLivePushDebugInfo.getUrl()).append("\n\n").append(getContext().getApplicationContext().getString(R.string.debug_system_data)).append("\n").append(getContext().getApplicationContext().getString(R.string.debug_cpu)).append(String.format("%.2f", Float.valueOf(alivcLivePushDebugInfo.getCpu())) + "%").append("\n").append(getContext().getApplicationContext().getString(R.string.debug_mem)).append(String.format("%.2f", Float.valueOf(alivcLivePushDebugInfo.getMemory())) + "MB").append("\n\n").append(getContext().getApplicationContext().getString(R.string.debug_basic_data)).append("\n").append(getContext().getApplicationContext().getString(R.string.debug_res)).append(alivcLivePushDebugInfo.getRes()).append("\n").append(getContext().getApplicationContext().getString(R.string.debug_spd)).append((alivcLivePushDebugInfo.getAudioUploadBitrate() + alivcLivePushDebugInfo.getVideoUploadBitrate()) + "Kbps").append("\n").append(getContext().getApplicationContext().getString(R.string.debug_aspd)).append(alivcLivePushDebugInfo.getAudioUploadBitrate() + "Kbps").append("\n").append(getContext().getApplicationContext().getString(R.string.debug_vspd)).append(alivcLivePushDebugInfo.getVideoUploadBitrate() + "Kbps").append("\n\n").append(getContext().getApplicationContext().getString(R.string.debug_queue_buffer)).append("\n").append(getContext().getApplicationContext().getString(R.string.debug_queue_videorender)).append(alivcLivePushDebugInfo.getVideoFramesInRenderBuffer()).append("\n").append(getContext().getApplicationContext().getString(R.string.debug_queue_videoencode)).append(alivcLivePushDebugInfo.getVideoFramesInEncodeBuffer()).append("\n").append(getContext().getApplicationContext().getString(R.string.debug_queue_audioencode)).append(alivcLivePushDebugInfo.getAudioFrameInEncodeBuffer()).append("\n").append(getContext().getApplicationContext().getString(R.string.debug_queue_videoupload)).append(alivcLivePushDebugInfo.getVideoPacketsInUploadBuffer()).append("\n").append(getContext().getApplicationContext().getString(R.string.debug_queue_audioupload)).append(alivcLivePushDebugInfo.getAudioPacketsInUploadBuffer()).append("\n\n").append(getContext().getApplicationContext().getString(R.string.debug_fps)).append("\n").append(getContext().getApplicationContext().getString(R.string.debug_fps_videocapture)).append(alivcLivePushDebugInfo.getVideoCaptureFps()).append("\n").append(getContext().getApplicationContext().getString(R.string.debug_fps_videorender)).append(alivcLivePushDebugInfo.getVideoRenderFps()).append("\n").append(getContext().getApplicationContext().getString(R.string.debug_fps_videoencode)).append(alivcLivePushDebugInfo.getVideoEncodeFps()).append("\n").append(getContext().getApplicationContext().getString(R.string.debug_fps_videoupload)).append(alivcLivePushDebugInfo.getVideoUploadeFps()).append("\n\n").append(getContext().getApplicationContext().getString(R.string.debug_drop_packet)).append("\n").append(getContext().getApplicationContext().getString(R.string.debug_drop_audioupload)).append(alivcLivePushDebugInfo.getTotalDroppedAudioFrames()).append("\n").append(getContext().getApplicationContext().getString(R.string.debug_drop_videoupload)).append(alivcLivePushDebugInfo.getTotalDurationOfDropingVideoFrames()).append("\n\n").append(getContext().getApplicationContext().getString(R.string.debug_bitrate_control)).append("\n").append(getContext().getApplicationContext().getString(R.string.debug_audiouploadlatest)).append(alivcLivePushDebugInfo.getLatestAudioBitrate() + "Kbps").append("\n").append(getContext().getApplicationContext().getString(R.string.debug_videouploadlatest)).append(alivcLivePushDebugInfo.getLatestVideoBitrate() + "Kbps").append("\n").append(getContext().getApplicationContext().getString(R.string.debug_buffersizelatest)).append(alivcLivePushDebugInfo.getSocketBufferSize()).append("\n").append(getContext().getApplicationContext().getString(R.string.debug_sendtimelatest)).append(alivcLivePushDebugInfo.getSocketSendTime());
            this.mText.setText(stringBuffer.toString());
        }
    }
}
