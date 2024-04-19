package com.alivc.debug;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;
import androidx.core.view.ViewCompat;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes.dex */
public class LineChartView extends View {
    private int brokenline_bottom;
    private int gridspace_heigh;
    private int gridspace_width;
    private int heigh;
    private Paint mPaint_bg;
    private Paint mPaint_brokenline;
    private Paint mPaint_brokenline1;
    private Paint mPaint_gridline;
    private Paint mPaint_path;
    private Paint mPaint_point_bg;
    private Paint mPaint_point_bg1;
    private Paint mPaint_point_sur;
    private Paint mPaint_text;
    private List<LineChartData> mdata;
    private List<LineChartData> mdata1;
    private Path mpath;
    private Path mpath1;
    private float size;
    private int width;

    public LineChartView(Context context) {
        super(context);
        this.mpath = new Path();
        this.mpath1 = new Path();
        this.mdata = new ArrayList();
        this.mdata1 = new ArrayList();
        this.size = 50.0f;
    }

    public LineChartView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mpath = new Path();
        this.mpath1 = new Path();
        this.mdata = new ArrayList();
        this.mdata1 = new ArrayList();
        this.size = 50.0f;
        init(context);
    }

    private void init(Context context) {
        Paint paint = new Paint(1);
        this.mPaint_bg = paint;
        paint.setColor(Color.argb(255, 239, 239, 239));
        Paint paint2 = new Paint(1);
        this.mPaint_gridline = paint2;
        paint2.setColor(Color.argb(255, 206, 203, 206));
        Paint paint3 = new Paint(1);
        this.mPaint_brokenline = paint3;
        paint3.setColor(Color.argb(255, 145, 200, 214));
        this.mPaint_brokenline.setTextSize(18.0f);
        this.mPaint_brokenline.setTextAlign(Paint.Align.CENTER);
        Paint paint4 = new Paint(1);
        this.mPaint_brokenline1 = paint4;
        paint4.setColor(Color.argb(255, 255, 0, 0));
        this.mPaint_brokenline1.setTextSize(18.0f);
        this.mPaint_brokenline1.setTextAlign(Paint.Align.CENTER);
        Paint paint5 = new Paint(1);
        this.mPaint_point_bg = paint5;
        paint5.setColor(Color.argb(255, 145, 200, 214));
        Paint paint6 = new Paint(1);
        this.mPaint_point_bg1 = paint6;
        paint6.setColor(Color.argb(255, 255, 0, 0));
        Paint paint7 = new Paint(1);
        this.mPaint_path = paint7;
        paint7.setColor(Color.argb(0, 145, 200, 214));
        Paint paint8 = new Paint(1);
        this.mPaint_point_sur = paint8;
        paint8.setColor(-1);
        Paint paint9 = new Paint(1);
        this.mPaint_text = paint9;
        paint9.setColor(ViewCompat.MEASURED_STATE_MASK);
        this.mPaint_text.setTextAlign(Paint.Align.CENTER);
        invalidate();
    }

    public List<LineChartData> getMdata() {
        return this.mdata;
    }

    public void setMdata(List<LineChartData> list) {
        this.mdata = list;
        requestLayout();
        invalidate();
    }

    public void setMdata(List<LineChartData> list, List<LineChartData> list2) {
        this.mdata = list;
        this.mdata1 = list2;
        requestLayout();
        invalidate();
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(-1);
        canvas.drawRect(10.0f, 0.0f, this.width, this.heigh - this.brokenline_bottom, this.mPaint_bg);
        for (int i = 0; i < 4; i++) {
        }
        drawPath(canvas);
        drawPath1(canvas);
    }

    private void drawPath(Canvas canvas) {
        for (int i = 0; i < this.mdata.size(); i++) {
            if (i == 0) {
                int i2 = this.heigh;
                int i3 = this.brokenline_bottom;
                this.mpath.moveTo((this.gridspace_width * i) + 10, (i2 - i3) - (((float) ((i2 - i3) * this.mdata.get(i).getValue())) / this.size));
            }
            if (i != this.mdata.size() - 1) {
                float f = (this.gridspace_width * i) + 10;
                int i4 = this.heigh;
                int i5 = this.brokenline_bottom;
                float value = (i4 - i5) - (((float) ((i4 - i5) * this.mdata.get(i).getValue())) / this.size);
                int i6 = i + 1;
                float f2 = (this.gridspace_width * i6) + 10;
                int i7 = this.heigh;
                int i8 = this.brokenline_bottom;
                canvas.drawLine(f, value, f2, (i7 - i8) - (((float) ((i7 - i8) * this.mdata.get(i6).getValue())) / this.size), this.mPaint_brokenline);
                int i9 = this.heigh;
                int i10 = this.brokenline_bottom;
                int i11 = this.heigh;
                int i12 = this.brokenline_bottom;
                this.mpath.quadTo((this.gridspace_width * i) + 10, (i9 - i10) - (((float) ((i9 - i10) * this.mdata.get(i).getValue())) / this.size), (this.gridspace_width * i6) + 10, (i11 - i12) - (((float) ((i11 - i12) * this.mdata.get(i6).getValue())) / this.size));
            }
            int i13 = this.heigh;
            int i14 = this.brokenline_bottom;
            canvas.drawCircle((this.gridspace_width * i) + 10, (i13 - i14) - (((float) ((i13 - i14) * this.mdata.get(i).getValue())) / this.size), 6.0f, this.mPaint_point_bg);
            int i15 = this.heigh;
            int i16 = this.brokenline_bottom;
            canvas.drawCircle((this.gridspace_width * i) + 10, (i15 - i16) - (((float) ((i15 - i16) * this.mdata.get(i).getValue())) / this.size), 3.0f, this.mPaint_point_sur);
            String str = this.mdata.get(i).getValue() + "";
            int i17 = this.heigh;
            int i18 = this.brokenline_bottom;
            canvas.drawText(str, (this.gridspace_width * i) + 10, ((i17 - i18) - (((float) ((i17 - i18) * this.mdata.get(i).getValue())) / this.size)) - this.mPaint_brokenline.measureText(str), this.mPaint_brokenline);
            canvas.drawText(this.mdata.get(i).getTime(), (this.gridspace_width * i) + 10, this.heigh - (this.brokenline_bottom / 2), this.mPaint_text);
            if (i == this.mdata.size() - 1) {
                int i19 = this.heigh;
                int i20 = this.brokenline_bottom;
                this.mpath.quadTo((this.gridspace_width * i) + 10, (i19 - i20) - (((float) ((i19 - i20) * this.mdata.get(i).getValue())) / this.size), (this.gridspace_width * i) + 10, this.heigh - this.brokenline_bottom);
                int i21 = this.heigh;
                int i22 = this.brokenline_bottom;
                this.mpath.quadTo((this.gridspace_width * i) + 10, i21 - i22, 10.0f, i21 - i22);
                this.mpath.close();
            }
        }
        canvas.drawPath(this.mpath, this.mPaint_path);
    }

    private void drawPath1(Canvas canvas) {
        for (int i = 0; i < this.mdata1.size(); i++) {
            if (i == 0) {
                int i2 = this.heigh;
                int i3 = this.brokenline_bottom;
                this.mpath1.moveTo((this.gridspace_width * i) + 10, (i2 - i3) - (((float) ((i2 - i3) * this.mdata1.get(i).getValue())) / this.size));
            }
            if (i != this.mdata1.size() - 1) {
                float f = (this.gridspace_width * i) + 10;
                int i4 = this.heigh;
                int i5 = this.brokenline_bottom;
                float value = (i4 - i5) - (((float) ((i4 - i5) * this.mdata1.get(i).getValue())) / this.size);
                int i6 = i + 1;
                float f2 = (this.gridspace_width * i6) + 10;
                int i7 = this.heigh;
                int i8 = this.brokenline_bottom;
                canvas.drawLine(f, value, f2, (i7 - i8) - (((float) ((i7 - i8) * this.mdata1.get(i6).getValue())) / this.size), this.mPaint_brokenline1);
                int i9 = this.heigh;
                int i10 = this.brokenline_bottom;
                int i11 = this.heigh;
                int i12 = this.brokenline_bottom;
                this.mpath1.quadTo((this.gridspace_width * i) + 10, (i9 - i10) - (((float) ((i9 - i10) * this.mdata1.get(i).getValue())) / this.size), (this.gridspace_width * i6) + 10, (i11 - i12) - (((float) ((i11 - i12) * this.mdata1.get(i6).getValue())) / this.size));
            }
            int i13 = this.heigh;
            int i14 = this.brokenline_bottom;
            canvas.drawCircle((this.gridspace_width * i) + 10, (i13 - i14) - (((float) ((i13 - i14) * this.mdata1.get(i).getValue())) / this.size), 6.0f, this.mPaint_point_bg1);
            int i15 = this.heigh;
            int i16 = this.brokenline_bottom;
            canvas.drawCircle((this.gridspace_width * i) + 10, (i15 - i16) - (((float) ((i15 - i16) * this.mdata1.get(i).getValue())) / this.size), 3.0f, this.mPaint_point_sur);
            String str = this.mdata1.get(i).getValue() + "";
            int i17 = this.heigh;
            int i18 = this.brokenline_bottom;
            canvas.drawText(str, (this.gridspace_width * i) + 10, (((i17 - i18) - (((float) ((i17 - i18) * this.mdata1.get(i).getValue())) / this.size)) - this.mPaint_brokenline1.measureText(str)) - 50.0f, this.mPaint_brokenline1);
            canvas.drawText(this.mdata1.get(i).getTime(), (this.gridspace_width * i) + 10, this.heigh - (this.brokenline_bottom / 2), this.mPaint_text);
            if (i == this.mdata1.size() - 1) {
                int i19 = this.heigh;
                int i20 = this.brokenline_bottom;
                this.mpath1.quadTo((this.gridspace_width * i) + 10, (i19 - i20) - (((float) ((i19 - i20) * this.mdata1.get(i).getValue())) / this.size), (this.gridspace_width * i) + 10, this.heigh - this.brokenline_bottom);
                int i21 = this.heigh;
                int i22 = this.brokenline_bottom;
                this.mpath1.quadTo((this.gridspace_width * i) + 10, i21 - i22, 10.0f, i21 - i22);
                this.mpath1.close();
            }
        }
        canvas.drawPath(this.mpath1, this.mPaint_path);
    }

    @Override // android.view.View
    protected void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
        this.gridspace_width = 50;
        if (this.mdata.size() == 0) {
            this.width = getDefaultSize(getSuggestedMinimumWidth(), i);
        } else {
            this.width = (this.gridspace_width * this.mdata.size()) + 10;
        }
        int defaultSize = getDefaultSize(getSuggestedMinimumHeight(), i2);
        this.heigh = defaultSize;
        this.brokenline_bottom = 50;
        this.gridspace_heigh = (defaultSize - 50) / 4;
        setMeasuredDimension(this.width, defaultSize);
    }

    public void setSize(float f) {
        this.size = f;
    }

    public float getSize() {
        return this.size;
    }
}
