package com.example.androidximalayafm.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;

/**
 * RoundRectImageView
 *
 * @author zhangfan
 * @email zhangfan.makesail@bytedance.com
 * @date 2020/7/21 9:47 AM
 * @since 1.0.0
 * <p>
 * Description:
 */
public class RoundRectImageView extends AppCompatImageView {
    private float roundRatio = 0.1f;
    private Path path;
    public RoundRectImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (path == null) {
            path = new Path();
            path.addRoundRect(new RectF(0,0,getWidth(), getHeight()), roundRatio*getWidth(), roundRatio*getHeight(), Path.Direction.CW);
        }
        canvas.save();
        canvas.clipPath(path);
        super.onDraw(canvas);
        canvas.restore();
    }
}
