package com.tc.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.tc.frescousedemo.R;

/**
 * Created by classTC on 12/26/2015. 实现圆角图片的容器
 */
public class RoundRelativeLayout extends RelativeLayout {

    private float radius;
    private boolean isPathValid;
    private Path mPath = new Path();

    public RoundRelativeLayout(Context context) {
        super(context);
    }

    public RoundRelativeLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.RoundRelativeLayout);
        radius = ta.getDimension(R.styleable.RoundRelativeLayout_radius, 0);
        ta.recycle();
    }


    @Override
    protected void dispatchDraw(Canvas canvas) {
        canvas.clipPath(getRoundRectPath());
        super.dispatchDraw(canvas);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.clipPath(getRoundRectPath());
        super.draw(canvas);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int oldWidth = getMeasuredWidth();
        int oldHeight = getMeasuredHeight();
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int newWidth = getMeasuredWidth();
        int newHeight = getMeasuredHeight();
        if (newWidth != oldWidth || newHeight != oldHeight) {
            isPathValid = false;
        }
    }

    private Path getRoundRectPath() {
        if (isPathValid) {
            return mPath;
        }

        mPath.reset();

        int width = getWidth();
        int height = getHeight();

        RectF bounds = new RectF(0, 0, width, height);
        mPath.addRoundRect(bounds, radius, radius, Path.Direction.CW);

        isPathValid = true;
        return mPath;
    }
}
