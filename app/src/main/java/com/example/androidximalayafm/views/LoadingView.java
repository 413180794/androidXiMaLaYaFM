package com.example.androidximalayafm.views;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.ImageView;

import androidx.appcompat.widget.AppCompatImageView;

import com.example.androidximalayafm.R;

/**
 * LogindView
 *
 * @author zhangfan
 * @email zhangfan.makesail@bytedance.com
 * @date 2020/7/31 10:03 AM
 * @since 1.0.0
 * <p>
 * Description:
 */
public class LogindView  extends AppCompatImageView {
    // 旋转的角度
    private int rotateDegree = 0;

    public LogindView(Context context) {
        this(context, null);
    }

    public LogindView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LogindView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // 设置图标
        setImageResource(R.mipmap.loading);
    }

    @Override
    protected void onAttachedToWindow() {
        // 绑定到 window 的时候
        super.onAttachedToWindow();
        post()
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        // 从 window 解绑的时候
    }

    @Override
    protected void onDraw(Canvas canvas) {
        /**
         * 第一个参数是旋转角度
         * 第二参数是旋转的 x 坐标
         * 第三个参数是旋转的 y 坐标
         */
        canvas.rotate(rotateDegree,getWidth() / 2, getHeight() / 2);
        super.onDraw(canvas);
    }
}
