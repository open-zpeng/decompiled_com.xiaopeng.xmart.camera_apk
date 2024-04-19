package com.alivc.debug;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.alivc.videochat.resource.R;
/* loaded from: classes.dex */
public class PushLogView extends LinearLayout {
    private TextView mClear;
    private TextView mClose;
    private TextView mText;

    public PushLogView(final Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.push_status_log, this);
        View findViewById = findViewById(R.id.push_status_log);
        this.mClose = (TextView) findViewById.findViewById(R.id.close);
        this.mText = (TextView) findViewById.findViewById(R.id.text);
        TextView textView = (TextView) findViewById.findViewById(R.id.clear);
        this.mClear = textView;
        textView.setVisibility(0);
        this.mText.setText("Start Debug...\n\n");
        this.mText.setTextColor(getResources().getColor(R.color.light_black));
        this.mClose.setOnClickListener(new View.OnClickListener() { // from class: com.alivc.debug.PushLogView.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                DebugViewManager.removePushLogWindow(context);
                DebugViewManager.createBigWindow(context);
            }
        });
        this.mClear.setOnClickListener(new View.OnClickListener() { // from class: com.alivc.debug.PushLogView.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                PushLogView.this.mText.setText("Start Debug...\n\n");
            }
        });
    }

    public void updateData(String str) {
        this.mText.setText(((Object) this.mText.getText()) + str + "\n\n");
    }
}
