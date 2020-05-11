package com.example.creditview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import java.util.ArrayList;


/**
 * 认证等级view
 */
public class CreditLevelView extends View {
    private float value;//当前值
    Context context;

    int end_radian = 180;

    Paint paintArc, paintCircle, paintText;
    public static final String LV_SMALL = " LV";
    public static final String LV_BIG = "LV";
    int x, y, r, w, h;
    float radius;
    Canvas canvas;
    ArrayList<Bitmap> bitmapList;
    private int start = 190, end = 350;

    public CreditLevelView(Context context) {
        super(context);
        this.context = context;
        initData();
    }

    public CreditLevelView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initData();
    }

    public CreditLevelView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        initData();
    }

    public void setValue(float level) {
        if (level > 0 && level <= 15) {
            if (level <= 1) {
                this.value = 10;
            } else if (level >= 1 && level <= 13) {
                this.value = (float) ((160 / 12.0) * level)-3;
            } else {
                this.value = 175;
            }
        } else {
            this.value = 0;
        }
        invalidate();
    }

    public void initData() {
        paintArc = new Paint();
        paintArc.setAntiAlias(true);
        paintArc.setDither(true);
        paintArc.setStyle(Paint.Style.STROKE);
        paintArc.setStrokeWidth(dp2px(3));
        paintArc.setColor(context.getResources().getColor(R.color.color_e6ebf4));

        paintCircle = new Paint();
        paintCircle.setAntiAlias(true);
        paintCircle.setDither(true);
        radius = dp2px(8) / 2f;
        paintCircle.setStyle(Paint.Style.FILL);
        paintCircle.setColor(context.getResources().getColor(R.color.green_1CCF9E));


        paintText = new Paint();
        paintText.setAntiAlias(true);
        paintText.setDither(true);
        paintText.setStyle(Paint.Style.FILL);
        paintText.setStrokeWidth(1);
        paintText.setTextSize(sp2px(12));
        paintText.setColor(context.getResources().getColor(R.color.color_recruit_detail_title));

        bitmapList = new ArrayList<>();

        Bitmap lv1 = BitmapFactory.decodeResource(getResources(), R.mipmap.base_credit_lv1);
        Bitmap lv2 = BitmapFactory.decodeResource(getResources(), R.mipmap.base_credit_lv2);
        Bitmap lv3 = BitmapFactory.decodeResource(getResources(), R.mipmap.base_credit_lv3);
        Bitmap lv4 = BitmapFactory.decodeResource(getResources(), R.mipmap.base_credit_lv4);
        Bitmap lv5 = BitmapFactory.decodeResource(getResources(), R.mipmap.base_credit_lv5);
        bitmapList.add(lv1);
        bitmapList.add(lv2);
        bitmapList.add(lv3);
        bitmapList.add(lv4);
        bitmapList.add(lv5);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        h = this.getMeasuredHeight();
        w = this.getMeasuredWidth();

        x = w / 2 - dp2px(60);
        y = h - dp2px(20);
        r = x;
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.canvas = canvas;

        paintArc.setColor(context.getResources().getColor(R.color.color_e6ebf4));
        canvas.drawArc(new RectF(dp2px(60), y - r, w - dp2px(60), y + r), end_radian, end_radian, false, paintArc);

        int count = 0, level;
        int x1, y1;
        for (int i = start; i <= end; i += 40) {
            x1 = (int) (x + r * Math.cos((-i) * -Math.PI / end_radian));
            y1 = (int) (y + r * Math.sin((-i) * -Math.PI / end_radian));

            if ((value + end_radian) < i) {
                paintCircle.setColor(context.getResources().getColor(R.color.color_e6ebf4));
            } else {
                paintCircle.setColor(context.getResources().getColor(R.color.green_1CCF9E));
            }
            canvas.drawCircle(x1 + dp2px(60), y1, radius, paintCircle);

            switch (count) {
                case 0:
                    x1 = x1 - dp2px(40);
                    y1 = y1 + dp2px(20);
                    break;
                case 1:
                    x1 = x1 - dp2px(40);
                    y1 = y1 + dp2px(5);
                    break;
                case 2:
                    x1 = x1 - dp2px(12);
                    y1 = y1 - dp2px(18);
                    break;
                case 3:
                    x1 = x1 + dp2px(20);
                    y1 = y1 + dp2px(5);
                    break;
                case 4:
                    x1 = x1 + dp2px(18);
                    y1 = y1 + dp2px(20);
                    break;
            }


            canvas.drawBitmap(bitmapList.get(count), x1 + dp2px(60), y1 - bitmapList.get(count).getHeight() - dp2px(14), paintText);

            level = count * 3 + 1;
            if (level < 10) {
                canvas.drawText(LV_SMALL + level, x1 + dp2px(60), y1, paintText);
            } else {
                canvas.drawText(LV_BIG + level, x1 + dp2px(60), y1, paintText);
            }
            count++;
        }


        paintArc.setColor(context.getResources().getColor(R.color.green_1CCF9E));
        canvas.drawArc(new RectF(dp2px(60), y - r, w - dp2px(60), y + r), end_radian, (int) value, false, paintArc);

    }


    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                Resources.getSystem().getDisplayMetrics());
    }

    private int sp2px(int sp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp,
                Resources.getSystem().getDisplayMetrics());
    }
}
